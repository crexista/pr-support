package st.crexi.application

import org.specs2.mutable.Specification
import st.crexi.adapter.ApplicationPersistence
import st.crexi.domain.PullRequestRepository
import st.crexi.domain.core.{PullRequestRepository, ReviewTask}
import st.crexi.domain.port.{ApplicationPersistence, PullRequestApplication}

class PullRequestApplicationSpec extends Specification{

	"PullRequestConverter" >> {
		"パースできる" >> {
			val topicReviewerTasks = Set(ReviewTask(false, "hoge"), ReviewTask(false, "fuga"))
			val childReviewerTasks = Set(ReviewTask(false, "hoge"), ReviewTask(true, "fuga"))

			val pullRequestApplication = new PullRequestApplication with ApplicationPersistence {
				override val repository: PullRequestRepository = null
			}


			val newReviewerTasks = pullRequestApplication.updateTopicReviewerStatus(topicReviewerTasks, childReviewerTasks, 10)
			println(newReviewerTasks)
			1 must_== 1
		}
	}
}
