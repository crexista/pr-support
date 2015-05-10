package st.crexi.Infrastructure

import spray.json.JsValue

import scala.language.dynamics


object JSONObject {

	def apply(jsValueSeq:Seq[JsValue]) = {
		new JSONObject(jsValueSeq)
	}


	def apply(jsValue:JsValue) = {
		new JSONObject(jsValue)
	}
}

class JSONObject private(val jsValueSeq:Seq[JsValue]) extends Dynamic {

	def this(jsValue:JsValue) = {
		this(Seq(jsValue))
	}

	def selectDynamic(name: String):JSONObject = {
		new JSONObject(jsValueSeq(0).asJsObject.getFields(name))
	}

	def asArray() = {
		jsValueSeq map { value =>
			new JSONObject(value)
		}
	}

	def apply(num:Int) = {
	}

	override def toString: String = {
		if (jsValueSeq.size == 1) jsValueSeq(0).toString().stripPrefix("\"").stripSuffix("\"")
		else jsValueSeq.foldLeft("[") { (prev, next) =>
			prev + "," + next.toString().stripPrefix("\"").stripSuffix("\"")
		} + "]"
	}
}
