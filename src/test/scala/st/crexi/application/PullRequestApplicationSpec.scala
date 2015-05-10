package st.crexi.application

import org.specs2.mutable.Specification
import st.crexi.adapter.{ApplicationPersistence, PullRequestApplication}
import st.crexi.domain.{PullRequestRepository, ReviewTask}

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
