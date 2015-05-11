package st.crexi.implementation

import spray.client.pipelining._
import spray.http.ContentTypes._
import spray.http._
import spray.json.{DefaultJsonProtocol, JsArray, JsonParser}
import st.crexi.Infrastructure.github.GithubAPIRequest
import st.crexi.domain.core.{PullRequestRepository, PullRequest, Project}
import st.crexi.domain.PullRequestRepository

import scala.concurrent.{ExecutionContext, Future}

object MyJsonProtocol extends DefaultJsonProtocol {
	implicit val colorFormat = jsonFormat1(UpdatePullRequest)
}

object SprayJsonPullRequestRepository {

	def apply()(implicit ec: ExecutionContext) = {
		implicit val request = GithubAPIRequest()
		new SprayJsonPullRequestRepository()
	}
}

class SprayJsonPullRequestRepository(implicit val restClient: HttpRequest => Future[HttpResponse], val ec: ExecutionContext) extends PullRequestRepository {

	import st.crexi.implementation.PullRequestConverter._

	def resolve(id: Int, baseURL: String): Future[PullRequest] = {
		restClient {
			Get(s"$baseURL/pulls/${id.toString}")
		} map { result =>
			JsonParser(result.entity.data.asString)
		}
	}

	def filterByHeadBranch(project:Project, name: String): Future[Seq[PullRequest]] = {
		val query = s"label:${project.owner}:$name"
		restClient {
			Get(s"${project.baseURL}/pulls?head=$query")
		} map { result =>
			val jsValue = JsonParser(result.entity.data.asString)
			jsValue.asInstanceOf[JsArray].elements map { value =>
				PullRequestConverter.pullRequestJsonToEntity(value)
			} toList
		}
	}

	def store(pullRequest: PullRequest): Future[Unit] = {

		val json = pullRequest
		val entity = HttpEntity(`application/json`, json)

		restClient {
			Patch(s"${pullRequest.project.baseURL}/pulls/${pullRequest.id.toString}", entity)
		} map { result =>}

	}
}
