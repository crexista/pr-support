package st.crexi.adapter


import spray.json.JsonParser
import spray.routing.HttpService
import st.crexi.Infrastructure.config.ConfigInitializer
import st.crexi.domain._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

// this trait defines our service behavior independently from the service actor
trait PullRequestService extends PullRequestAdapter with HttpService {

	import converter._
	import port._
	ConfigInitializer("application.conf")

	val route = path("pr") {
		post { implicit ctx =>

			// githubから飛んできたJsonをここで変換
			val notification: PullRequestEvent = JsonParser(ctx.request.entity.data.asString)

			// Default ブランチむけのプルリクであれば何もせずおしまい
			if (isDefaultBranchPullRequest(notification.pullRequest)) {
				ctx.complete("Default Branch PullRequest")
			} else {

				//プルリクのステータスでハンドリング
				notification.action match {
					case Other => ctx.complete("ignore")
					case _ => updateTopicPullRequest(notification) onComplete {
						case Success(pr) => ctx.complete("dosukoi")
						case Failure(error) => ctx.complete("error::" + error.getStackTrace)
					}
				}
			}

		} ~
			get {
				complete("hogee")
			}
	}
}




