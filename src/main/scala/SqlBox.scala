import java.sql.{Connection, PreparedStatement, ResultSet}

/**
  *
  */

object SqlBox {

  case class SqlBoxRawValue(value:String)
  case class SqlBoxValueWrapper(wrapper:String, value:Any)

  def apply(sql:String, params:Any*):SqlBox = {
    val sqlBox = new SqlBox()
    sqlBox.append(sql, params:_*)
    return sqlBox
  }

  def createInsert(tb:String, map: Map[String, Any]):SqlBox = {
    val sqlBox = new SqlBox()
    val columns = new StringBuilder()
    val values = new StringBuilder()
    map.foreach((kv) => {
      columns ++= kv._1 ++= ","
      kv._2 match {
        case SqlBoxRawValue(val1:String) => {
          values ++= val1 ++= ","
        }
        case SqlBoxValueWrapper(wrapper:String, val1:Any) => {
          values ++= wrapper ++= ","
          sqlBox.putParam(true, val1)
        }
        case _ => {
          values ++= "?,"
          sqlBox.putParam(true, kv._2)
        }
      }
    })
    if (columns.length > 0) {
      columns.deleteCharAt(columns.length - 1)
    }
    if (values.length > 0) {
      values.deleteCharAt(values.length - 1)
    }
    val sql = s"insert into $tb ($columns) values ($values)".toString
    sqlBox.sql = sql
    return sqlBox
  }
}

class SqlBox {

  val _paramList = collection.mutable.ListBuffer[Any]()
  //var _sql:String = null
  val _sqlBuilder:StringBuilder = new StringBuilder();

  def putParam(condition:Boolean, params:Any*):Boolean = {
    if (condition) {
      params.foreach((param:Any) => {_paramList += param})
    }
    return condition
  }

  def putWhenContainsKey(params:Map[String, Any], keys:String*):Boolean = {
    var checkResult = true
    keys.foreach((key) => {
      if (!params.contains(key)) {
        checkResult = false
      }
    })
    if (checkResult) {
      keys.foreach((key) => {
        _paramList += params(key)
      })
      return true
    }else {
      return false
    }
  }

  def append(clause:String, params:Any*) = {
    if (clause != null) {
      _sqlBuilder.append(clause)
    }
    if (params != null) {
      params.foreach((param:Any) => {_paramList += param})
    }
  }

  def appendLine(clause:String, params:Any*) = {
    val clauseToAppend:String = if (clause==null) null else (clause + "\n")
    this.append(clauseToAppend, params)
  }

  def paramList:List[Any] = this._paramList.toList

  def sql_=(sql:String) = {
    //_sql = sql
    _sqlBuilder.clear()
    _sqlBuilder.append(sql);
  }

  def sql = {
    _sqlBuilder.toString()
  }

  def setStatementParams(stmt:PreparedStatement) = {
    (1 to _paramList.size).foreach((idx) => {stmt.setObject(idx, _paramList(idx-1))})
  }

  case class QueryResult(statement:PreparedStatement, resultSet:ResultSet)
  case class UpdateResult(statement:PreparedStatement, rows:Int)

  def query(connection:Connection):QueryResult = {
    val stmt = connection.prepareStatement(this.sql)
    this.setStatementParams(stmt)
    val resultSet = stmt.executeQuery()
    return new QueryResult(stmt, resultSet)
  }

  def update(connection: Connection):UpdateResult = {
    val stmt = connection.prepareStatement(this.sql)
    this.setStatementParams(stmt)
    val rows = stmt.executeUpdate()
    return new UpdateResult(stmt, rows)
  }
}
