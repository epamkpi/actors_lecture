import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Procedure;

public class HappyAngryActor extends UntypedActor {

    public static Props mkProps() {
        return Props.create(HappyAngryActor.class);
    }

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            if (message.equals("bad")) {
                System.out.println("I feel bad");
                getContext().become(angry);
            } else if (message.equals("good")) {
                System.out.println("I am already happy");
                getSelf().tell("I am already happy", getSelf());
            }
        }
    };

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            if (message.equals("bad")) {
                System.out.println("I am already angry");
                getSender().tell("I am already angry", getSelf());
            } else if (message.equals("good")) {
                System.out.println("I feel better");
                getContext().become(happy);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals("good")) {
            getContext().become(happy);
        } else if (message.equals("bad")) {
            getContext().become(angry);
        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef actor = system.actorOf(HappyAngryActor.mkProps(), "DeviantActor");
        actor.tell("good", ActorRef.noSender());
        actor.tell("good", ActorRef.noSender());
        actor.tell("bad", ActorRef.noSender());
        actor.tell("bad", ActorRef.noSender());
        system.shutdown();
    }
}