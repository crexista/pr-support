package st.crexi.implementation

import spray.json.{DefaultJsonProtocol, JsValue}
import st.crexi.Infrastructure.JSONObject
import st.crexi.domain._

import scala.util.matching.Regex


object JSProtocol extends DefaultJsonProtocol {
	implicit val colorFormat = jsonFormat1(UpdatePullRequest)
}

object PullRequestConverter {

	val contentTitle = "このpull requestが解決する内容"
	val checkMethodTitle = "動作確認方法"
	val ticketTitle = "チケット"
	val relationalPullRequest = "関連プルリク"
	val reviewerTitle = "完了条件(全てチェックしたらマージしてよい)"

	implicit def eventJsonToNotification(jsValue:JsValue): PullRequestEvent = {
		val jsObj = JSONObject(jsValue)
		val action = checkToAction(jsObj.action.toString)
		val pullReqJson = jsObj.pull_request

		PullRequestEvent(action, pullRequestJsonToEntity(pullReqJson.jsValueSeq(0)))
	}


	implicit def pullRequestJsonToEntity(jsValue:JsValue): PullRequest = {
		val pullReqJson = JSONObject(jsValue)
		val body = parseBodyString(pullReqJson.body.toString)
		val number = pullReqJson.number.toString.toInt
		val title = pullReqJson.title.toString
		val branch = pullReqJson.base.ref.toString
		val project = Project(
			pullReqJson.base.repo.id.toString.toInt,
			pullReqJson.base.repo.name.toString,
			pullReqJson.base.repo.owner.login.toString,
			pullReqJson.base.repo.default_branch.toString,
			pullReqJson.base.repo.url.toString
		)
		PullRequest(number, title, body, project, branch)
	}



	implicit def pullRequestToPatchJsonString(pullRequest:PullRequest): String = {
		import spray.json._
		import st.crexi.implementation.JSProtocol._

		val taskString = pullRequest.body.reviewTask.foldLeft(""){ (prev, next) =>
			val x = if (next.completed) "x" else " "
			val listed = prev + "- [" + x + "]" + " @" + next.reviewer

			val nums = next.unCompleted map { number =>
				s"#${number.toString}"
			}

			listed + (if (nums.size == 0) "" else s" ${nums.toString().stripPrefix("Set")}") + "\r\n"
		}

		val baseBody =
			s"""### $contentTitle
				 |${pullRequest.body.content}
				 |
				 |### $checkMethodTitle
				 |${pullRequest.body.checkMethod}
				 |
				 |### $ticketTitle
				 |${pullRequest.body.ticketName}
				 |
				 |### $reviewerTitle
				 |$taskString
				 |
			 """.stripMargin

		val relations = pullRequest.body.relation map { relations =>
			relations.foldLeft("\r\n### " + "関連プルリク\r\n") { (prev, next) =>
				val x = if (next.completed) "x" else " "
				prev + "- [" + x + "]" + " #" + next.pullRequestNumber.toString + "\r\n"
			}
		}
		val bodyString = baseBody + relations.getOrElse("")
		println(bodyString)
		UpdatePullRequest(bodyString).toJson.asJsObject.toString()
	}

	def checkToAction(str:String) = {
		str  match {
			case "opened" => Opened
			case "closed" => Closed
			case _ => Other
		}
	}


	def extract(str:String, prev:Regex.Match, next:Regex.Match) = {
		str.substring(prev.start + prev.group(0).length, next.start).replaceFirst("\n", "")
	}


	def parseRelationalPR(str:String, prev:Regex.Match, next:Regex.Match) = {
		val r2 = """\n?\-\s\[([\w|\s])\]\s#(\d)[\r|\n]?""".r
		val r3 = """\r\n""".r
		val bstr = extract(str, prev, next)
		r3.split(bstr).toList.map {
			case r2(check, name) => MergeTask(check == "x", name.toInt)
			case _ => MergeTask(false, -1)
		} filter { task =>
			task.pullRequestNumber != -1
		}
	}


	def parseReviewTask(str:String, prev:Regex.Match, next:Regex.Match) = {
		val r2 = """\n?\-\s\[([\w|\s])\]\s@([^\s]*)\s?\(?([^\)]*)\)?[\r|\n]?""".r
		val r3 = """\r\n""".r
		val bstr = extract(str, prev, next)
		println(bstr)
		r3.split(bstr).toList.map {
			case r2(check, name, children) => {
				val r4 = """#(\d*)""".r
				val relationalTasks = r4.findAllIn(children).matchData.foldLeft(Set.empty[Int]){ (set, regex) =>
					set + regex.group(1).toInt
				}

				ReviewTask(check == "x", name, relationalTasks)
			}
			case _ => ReviewTask(false, "")
		} filter { task =>
			task.reviewer != ""
		}
	}

	def parseBodyString(str:String) = {
		val str2 = str.replace("\\r", "\r").replace("\\n", "\n")
		val str3 = "\n" + str2 + "\n### a\r\n\r\nb"
		val regex = """(\n#{3}|^###)\s([^\n]*)""".r
		val matchData = regex.findAllIn(str3).matchData
		matchData.foldLeft(BodyParser(None, null)) { (prev, next) =>
			prev.regex.map { regex =>
				regex.group(0).substring(5).stripSuffix("\r") match {
					case `contentTitle`          =>
						prev.body = prev.body.copy(content = extract(str3, regex, next))

					case `checkMethodTitle`      =>
						prev.body = prev.body.copy(checkMethod = extract(str3, regex, next))

					case `ticketTitle`           =>
						prev.body = prev.body.copy(ticketName = extract(str3, regex, next))

					case `relationalPullRequest` =>
						prev.body = prev.body.copy(relation = Option(parseRelationalPR(str3, regex, next).toSet))

					case `reviewerTitle`         =>
						prev.body = prev.body.copy(reviewTask = parseReviewTask(str3, regex, next).toSet)

					case _:String => println("")
				}
				BodyParser(Option(next), prev.body)
			} getOrElse BodyParser(Option(next), PullRequestBody(null, null, null, null))
		} .body
	}
}

case class BodyParser(regex:Option[Regex.Match], var body:PullRequestBody = null)