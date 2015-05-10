package st.crexi.Infrastructure.github

import akka.actor.ActorSystem

import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import st.crexi.Infrastructure.config.{ConfigInitializer, InfraConfig}

import scala.concurrent.{ExecutionContext, Future}

object GithubAPIRequest {
	implicit val system = ActorSystem("rest-client")

	def apply()(implicit ec: ExecutionContext) = {
		implicit val token = InfraConfig.githubToken
		val pipeline: HttpRequest => Future[HttpResponse] = {
			addHeader("Authorization", s"token $token") ~>
				sendReceive
		}
		pipeline
	}


}
