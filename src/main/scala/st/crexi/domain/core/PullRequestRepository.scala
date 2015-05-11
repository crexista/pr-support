package st.crexi.domain.core

import scala.concurrent.Future


/**
 * Projectに出されたPullRequestの永続化及び検索をおこなうrepositoryです
 */
trait PullRequestRepository {

	def resolve(id: Int, baseURL: String): Future[PullRequest]

	def filterByHeadBranch(project:Project, name:String): Future[Seq[PullRequest]]

	def store(pullRequest:PullRequest): Future[Unit]
}
