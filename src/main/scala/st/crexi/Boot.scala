package st.crexi

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import spray.servlet.WebBoot
import st.crexi.Infrastructure.config.ConfigInitializer
import st.crexi.PullRequestServiceActor

// this class is instantiated by the servlet initializer
// it needs to have a default constructor and implement
// the spray.servlet.WebBoot trait
class Boot extends WebBoot {

	ConfigInitializer("application.conf")

	// we need an ActorSystem to host our application in
	val system = ActorSystem("example")

	// the service actor replies to incoming HttpRequests
	val serviceActor = system.actorOf(Props[PullRequestServiceActor])

}