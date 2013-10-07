import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

public class SimpleActorExample {

    public static class SimpleActor extends UntypedActor {

        private int count = 0;
        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof Integer) {
                count += (Integer) message;
                System.out.println(count);
                getSender().tell(message, getSelf());
            }
        }
    }

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("SimpleActorSystem");
        final ActorRef actor = system.actorOf(Props.create(SimpleActor.class), "ActorName");

        for (int i = 0; i < 5; i++) {
            actor.tell(new Integer(i),  ActorRef.noSender());
        }

        system.shutdown();
    }
}

