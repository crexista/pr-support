package st.crexi.adapter

import st.crexi.domain.core.PullRequestRepository
import st.crexi.domain.port.{ApplicationPersistence, PullRequestApplication}
import st.crexi.implementation.{SprayJsonPullRequestRepository, PullRequestConverter}

trait PullRequestAdapter {
	val converter = PullRequestConverter

	val port = new PullRequestApplication with ApplicationPersistence {
		import scala.concurrent.ExecutionContext.Implicits._

		val repository: PullRequestRepository = SprayJsonPullRequestRepository()
	}

}
