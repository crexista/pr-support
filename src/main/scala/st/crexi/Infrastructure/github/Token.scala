package st.crexi.Infrastructure.github

case class Token(value:String) {
	override def toString = value
}


object Token {
	implicit def stringToToken(str: String): Token = Token(str)
	implicit def tokenToString(token: Token): String = token.value
}