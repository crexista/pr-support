package st.crexi.adapter

import st.crexi.domain._

import scala.concurrent.ExecutionContext.Implicits.global

trait PullRequestApplication {
	self: ApplicationPersistence =>

	def updateTopicPullRequest(notification:PullRequestEvent) = {
		val pullRequest = notification.pullRequest

		repository.filterByHeadBranch(pullRequest.project, pullRequest.destinationBranch) flatMap { results =>
			val topicPullReq = results(0)
			val mergeTask = MergeTask(notification.action == Closed, pullRequest.id)

			val tasks = topicPullReq.body.relation match {
				case Some(list) =>
					val set = list.toSet
					if (set(mergeTask)) set - mergeTask + mergeTask else set + mergeTask

				case None => Set(mergeTask)
			}

			val reviewerTasks = updateTopicReviewerStatus(
				topicPullReq.body.reviewTask,
				pullRequest.body.reviewTask,
				pullRequest.id)
			repository.store(topicPullReq.update(Option(tasks), reviewerTasks))
		}
	}

	def updateTopicReviewerStatus(topicReviewTasks:Set[ReviewTask], childReviewTask:Set[ReviewTask], childPullReqNum:Int) = {

		val taskMap = childReviewTask.foldLeft(Map.empty[String, ReviewTask]) { (prev, next) =>
			prev + (next.reviewer -> next)
		}

		topicReviewTasks map { topicTask =>
			taskMap.get(topicTask.reviewer) map { childTask =>
				if (!childTask.completed) topicTask.copy(unCompleted = topicTask.unCompleted + childPullReqNum)
				else topicTask.copy(unCompleted = topicTask.unCompleted - childPullReqNum)
			} getOrElse topicTask
		}

	}

	def isDefaultBranchPullRequest(pullReq:PullRequest):Boolean = {
		pullReq.destinationBranch == pullReq.project.defaultBranch
	}

}

trait ApplicationPersistence {
	val repository:PullRequestRepository
}
