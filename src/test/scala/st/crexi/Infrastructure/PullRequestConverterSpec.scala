package st.crexi.Infrastructure

import org.specs2.mutable.Specification
import spray.json.JsonParser
import st.crexi.domain.core.Opened
import st.crexi.implementation.PullRequestConverter


class PullRequestConverterSpec extends Specification{

	val sampleJson = """{"action":"opened","number":3,"pull_request":{"url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3","id":73673,"html_url":"http://github.com/crexista/PullReqTest/pull/3","diff_url":"http://github.com/crexista/PullReqTest/pull/3.diff","patch_url":"http://github.com/crexista/PullReqTest/pull/3.patch","issue_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/3","number":3,"state":"open","locked":false,"title":"[XXX-aaa] どすこいどすこい","user":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"body":"### このpull requestが解決する内容\r\nうむー\r\n\r\n### 完了条件(全てチェックしたらマージしてよい)\r\n- [x] @hoge (#12, #23)\r\n- [ ] @fuga\r\n\r\n### 動作確認方法\r\nとりえあず\r\n\r\n### チケット\r\nhttps://ticket.exp.com/jira/browse/EXP-3762","created_at":"2015-05-05T20:19:47Z","updated_at":"2015-05-05T20:19:48Z","closed_at":null,"merged_at":null,"merge_commit_sha":null,"assignee":null,"milestone":null,"commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3/commits","review_comments_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3/comments","review_comment_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/comments/{number}","comments_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/3/comments","statuses_url":"http://github.com/api/v3/repos/crexista/PullReqTest/statuses/251ad3264714fb3e323c591c0df463126792f407","head":{"label":"crexista:sample3","ref":"sample3","sha":"251ad3264714fb3e323c591c0df463126792f407","user":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"repo":{"id":9355,"name":"PullReqTest","full_name":"crexista/PullReqTest","owner":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"private":false,"html_url":"http://github.com/crexista/PullReqTest","description":"","fork":false,"url":"http://github.com/api/v3/repos/crexista/PullReqTest","forks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/forks","keys_url":"http://github.com/api/v3/repos/crexista/PullReqTest/keys{/key_id}","collaborators_url":"http://github.com/api/v3/repos/crexista/PullReqTest/collaborators{/collaborator}","teams_url":"http://github.com/api/v3/repos/crexista/PullReqTest/teams","hooks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/hooks","issue_events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/events{/number}","events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/events","assignees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/assignees{/user}","branches_url":"http://github.com/api/v3/repos/crexista/PullReqTest/branches{/branch}","tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/tags","blobs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/blobs{/sha}","git_tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/tags{/sha}","git_refs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/refs{/sha}","trees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/trees{/sha}","statuses_url":"http://github.com/api/v3/repos/crexista/PullReqTest/statuses/{sha}","languages_url":"http://github.com/api/v3/repos/crexista/PullReqTest/languages","stargazers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/stargazers","contributors_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contributors","subscribers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscribers","subscription_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscription","commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/commits{/sha}","git_commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/commits{/sha}","comments_url":"http://github.com/api/v3/repos/crexista/PullReqTest/comments{/number}","issue_comment_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/comments/{number}","contents_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contents/{+path}","compare_url":"http://github.com/api/v3/repos/crexista/PullReqTest/compare/{base}...{head}","merges_url":"http://github.com/api/v3/repos/crexista/PullReqTest/merges","archive_url":"http://github.com/api/v3/repos/crexista/PullReqTest/{archive_format}{/ref}","downloads_url":"http://github.com/api/v3/repos/crexista/PullReqTest/downloads","issues_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues{/number}","pulls_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls{/number}","milestones_url":"http://github.com/api/v3/repos/crexista/PullReqTest/milestones{/number}","notifications_url":"http://github.com/api/v3/repos/crexista/PullReqTest/notifications{?since,all,participating}","labels_url":"http://github.com/api/v3/repos/crexista/PullReqTest/labels{/name}","releases_url":"http://github.com/api/v3/repos/crexista/PullReqTest/releases{/id}","created_at":"2015-05-02T15:02:51Z","updated_at":"2015-05-02T15:02:51Z","pushed_at":"2015-05-05T20:19:48Z","git_url":"git://github.com/crexista/PullReqTest.git","ssh_url":"git@github.com:crexista/PullReqTest.git","clone_url":"http://github.com/crexista/PullReqTest.git","svn_url":"http://github.com/crexista/PullReqTest","homepage":null,"size":324,"stargazers_count":0,"watchers_count":0,"language":null,"has_issues":true,"has_downloads":true,"has_wiki":true,"has_pages":false,"forks_count":0,"mirror_url":null,"open_issues_count":2,"forks":0,"open_issues":2,"watchers":0,"default_branch":"master"}},"base":{"label":"crexista:sample","ref":"sample","sha":"eb0f5e5bb5c2c347dc103b5b80a83c293048a681","user":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"repo":{"id":9355,"name":"PullReqTest","full_name":"crexista/PullReqTest","owner":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"private":false,"html_url":"http://github.com/crexista/PullReqTest","description":"","fork":false,"url":"http://github.com/api/v3/repos/crexista/PullReqTest","forks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/forks","keys_url":"http://github.com/api/v3/repos/crexista/PullReqTest/keys{/key_id}","collaborators_url":"http://github.com/api/v3/repos/crexista/PullReqTest/collaborators{/collaborator}","teams_url":"http://github.com/api/v3/repos/crexista/PullReqTest/teams","hooks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/hooks","issue_events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/events{/number}","events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/events","assignees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/assignees{/user}","branches_url":"http://github.com/api/v3/repos/crexista/PullReqTest/branches{/branch}","tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/tags","blobs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/blobs{/sha}","git_tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/tags{/sha}","git_refs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/refs{/sha}","trees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/trees{/sha}","statuses_url":"http://github.com/api/v3/repos/crexista/PullReqTest/statuses/{sha}","languages_url":"http://github.com/api/v3/repos/crexista/PullReqTest/languages","stargazers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/stargazers","contributors_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contributors","subscribers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscribers","subscription_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscription","commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/commits{/sha}","git_commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/commits{/sha}","comments_url":"http://github.com/api/v3/repos/crexista/PullReqTest/comments{/number}","issue_comment_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/comments/{number}","contents_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contents/{+path}","compare_url":"http://github.com/api/v3/repos/crexista/PullReqTest/compare/{base}...{head}","merges_url":"http://github.com/api/v3/repos/crexista/PullReqTest/merges","archive_url":"http://github.com/api/v3/repos/crexista/PullReqTest/{archive_format}{/ref}","downloads_url":"http://github.com/api/v3/repos/crexista/PullReqTest/downloads","issues_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues{/number}","pulls_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls{/number}","milestones_url":"http://github.com/api/v3/repos/crexista/PullReqTest/milestones{/number}","notifications_url":"http://github.com/api/v3/repos/crexista/PullReqTest/notifications{?since,all,participating}","labels_url":"http://github.com/api/v3/repos/crexista/PullReqTest/labels{/name}","releases_url":"http://github.com/api/v3/repos/crexista/PullReqTest/releases{/id}","created_at":"2015-05-02T15:02:51Z","updated_at":"2015-05-02T15:02:51Z","pushed_at":"2015-05-05T20:19:48Z","git_url":"git://github.com/crexista/PullReqTest.git","ssh_url":"git@github.com:crexista/PullReqTest.git","clone_url":"http://github.com/crexista/PullReqTest.git","svn_url":"http://github.com/crexista/PullReqTest","homepage":null,"size":324,"stargazers_count":0,"watchers_count":0,"language":null,"has_issues":true,"has_downloads":true,"has_wiki":true,"has_pages":false,"forks_count":0,"mirror_url":null,"open_issues_count":2,"forks":0,"open_issues":2,"watchers":0,"default_branch":"master"}},"_links":{"self":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3"},"html":{"href":"http://github.com/crexista/PullReqTest/pull/3"},"issue":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/3"},"comments":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/3/comments"},"review_comments":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3/comments"},"review_comment":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/comments/{number}"},"commits":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls/3/commits"},"statuses":{"href":"http://github.com/api/v3/repos/crexista/PullReqTest/statuses/251ad3264714fb3e323c591c0df463126792f407"}},"merged":false,"mergeable":null,"mergeable_state":"unknown","merged_by":null,"comments":0,"review_comments":0,"commits":1,"additions":1,"deletions":0,"changed_files":1},"repository":{"id":9355,"name":"PullReqTest","full_name":"crexista/PullReqTest","owner":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false},"private":false,"html_url":"http://github.com/crexista/PullReqTest","description":"","fork":false,"url":"http://github.com/api/v3/repos/crexista/PullReqTest","forks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/forks","keys_url":"http://github.com/api/v3/repos/crexista/PullReqTest/keys{/key_id}","collaborators_url":"http://github.com/api/v3/repos/crexista/PullReqTest/collaborators{/collaborator}","teams_url":"http://github.com/api/v3/repos/crexista/PullReqTest/teams","hooks_url":"http://github.com/api/v3/repos/crexista/PullReqTest/hooks","issue_events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/events{/number}","events_url":"http://github.com/api/v3/repos/crexista/PullReqTest/events","assignees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/assignees{/user}","branches_url":"http://github.com/api/v3/repos/crexista/PullReqTest/branches{/branch}","tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/tags","blobs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/blobs{/sha}","git_tags_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/tags{/sha}","git_refs_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/refs{/sha}","trees_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/trees{/sha}","statuses_url":"http://github.com/api/v3/repos/crexista/PullReqTest/statuses/{sha}","languages_url":"http://github.com/api/v3/repos/crexista/PullReqTest/languages","stargazers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/stargazers","contributors_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contributors","subscribers_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscribers","subscription_url":"http://github.com/api/v3/repos/crexista/PullReqTest/subscription","commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/commits{/sha}","git_commits_url":"http://github.com/api/v3/repos/crexista/PullReqTest/git/commits{/sha}","comments_url":"http://github.com/api/v3/repos/crexista/PullReqTest/comments{/number}","issue_comment_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues/comments/{number}","contents_url":"http://github.com/api/v3/repos/crexista/PullReqTest/contents/{+path}","compare_url":"http://github.com/api/v3/repos/crexista/PullReqTest/compare/{base}...{head}","merges_url":"http://github.com/api/v3/repos/crexista/PullReqTest/merges","archive_url":"http://github.com/api/v3/repos/crexista/PullReqTest/{archive_format}{/ref}","downloads_url":"http://github.com/api/v3/repos/crexista/PullReqTest/downloads","issues_url":"http://github.com/api/v3/repos/crexista/PullReqTest/issues{/number}","pulls_url":"http://github.com/api/v3/repos/crexista/PullReqTest/pulls{/number}","milestones_url":"http://github.com/api/v3/repos/crexista/PullReqTest/milestones{/number}","notifications_url":"http://github.com/api/v3/repos/crexista/PullReqTest/notifications{?since,all,participating}","labels_url":"http://github.com/api/v3/repos/crexista/PullReqTest/labels{/name}","releases_url":"http://github.com/api/v3/repos/crexista/PullReqTest/releases{/id}","created_at":"2015-05-02T15:02:51Z","updated_at":"2015-05-02T15:02:51Z","pushed_at":"2015-05-05T20:19:48Z","git_url":"git://github.com/crexista/PullReqTest.git","ssh_url":"git@github.com:crexista/PullReqTest.git","clone_url":"http://github.com/crexista/PullReqTest.git","svn_url":"http://github.com/crexista/PullReqTest","homepage":null,"size":324,"stargazers_count":0,"watchers_count":0,"language":null,"has_issues":true,"has_downloads":true,"has_wiki":true,"has_pages":false,"forks_count":0,"mirror_url":null,"open_issues_count":2,"forks":0,"open_issues":2,"watchers":0,"default_branch":"master"},"sender":{"login":"crexista","id":249,"avatar_url":"http://github.com/avatars/u/249?","gravatar_id":"","url":"http://github.com/api/v3/users/crexista","html_url":"http://github.com/crexista","followers_url":"http://github.com/api/v3/users/crexista/followers","following_url":"http://github.com/api/v3/users/crexista/following{/other_user}","gists_url":"http://github.com/api/v3/users/crexista/gists{/gist_id}","starred_url":"http://github.com/api/v3/users/crexista/starred{/owner}{/repo}","subscriptions_url":"http://github.com/api/v3/users/crexista/subscriptions","organizations_url":"http://github.com/api/v3/users/crexista/orgs","repos_url":"http://github.com/api/v3/users/crexista/repos","events_url":"http://github.com/api/v3/users/crexista/events{/privacy}","received_events_url":"http://github.com/api/v3/users/crexista/received_events","type":"User","site_admin":false}}"""

	"PullRequestConverter" >> {
		"パースできる" >> {
			val json = JsonParser(sampleJson)
			val notification = PullRequestConverter.eventJsonToNotification(json)
			println(notification.pullRequest)
			println(PullRequestConverter.pullRequestToPatchJsonString(notification.pullRequest))
			notification.action must_== Opened
			notification.pullRequest.id must_== 3
			notification.pullRequest.title must_== "[XXX-aaa] どすこいどすこい"
		}
	}
}

case class Hoge(a:String, b:String) {
	override def equals(other:Any) = other match {
		case that: Hoge => (that canEqual this) && (this.b == that.b)
		case _ => false
	}
}