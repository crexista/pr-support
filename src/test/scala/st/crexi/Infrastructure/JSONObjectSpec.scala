package st.crexi.Infrastructure

import org.specs2.mutable.Specification
import spray.json.JsonParser

class JSONObjectSpec extends Specification {
	val sampleJson = "{\"hoge\":{\"fuga\":{\"foo\":\"bar\",\"piyo\":[\"a\",\"b\"]}}}"

	"JSONObject" >> {
		sampleJson + "にドットアクセス演算子でアクセスした場合" >> {
			val jsValue = JsonParser(sampleJson)
			val jsObj = JSONObject(jsValue)

			"hoge.fuga.fooでbarを返す" >> {
				jsObj.hoge.fuga.foo.toString must_== "bar"
			}

			"hoge.fugaではJsonオブジェクトを返す" >> {
				jsObj.hoge.toString must_== "{\"fuga\":{\"foo\":\"bar\",\"piyo\":[\"a\",\"b\"]}}"
			}
		}
	}

}
