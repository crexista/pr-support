package st.crexi.adapter

import st.crexi.domain.PullRequestRepository
import st.crexi.implementation.{SprayJsonPullRequestRepository, PullRequestConverter}

trait PullRequestAdapter {
	val converter = PullRequestConverter

	val port = new PullRequestApplication with ApplicationPersistence {
		import scala.concurrent.ExecutionContext.Implicits._

		val repository: PullRequestRepository = SprayJsonPullRequestRepository()
	}

}
