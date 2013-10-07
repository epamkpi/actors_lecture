package DinningPhilosophers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Philosopher extends UntypedActor {
    public static Props mkProps(String aName, ActorRef aWeiter) {
        return Props.create(Philosopher.class, aName, aWeiter);
    }

    private String name;
    private ActorRef weiter;
    private static final int THINK_TIME = 3000;
    private static final int EAT_TIME = 3000;

    private Philosopher(String aName, ActorRef aWeiter) {
        name = aName;
        weiter = aWeiter;
        // Let`s introduce ourselves to Waiter
        aWeiter.tell(new Messages.Introduce(aName), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Messages.Think) {
            System.out.println(name + " thinking");
            Thread.sleep(THINK_TIME);
            System.out.println(name + " gets hungry");
            weiter.tell(new Messages.Hungry(), getSelf());

        } else if (message instanceof Messages.Eat) {
            System.out.println(name + " eating");
            Thread.sleep(EAT_TIME);
            System.out.println(name + " fed up");
            weiter.tell(new Messages.FinishEat(), getSelf());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create();
        final int FORKS = 5;
        ActorRef waiter = system.actorOf(Waiter.mkProps(FORKS));

        ActorRef Socrates   = system.actorOf(Philosopher.mkProps("Socrates", waiter));
        ActorRef Aristotle  = system.actorOf(Philosopher.mkProps("Aristotle", waiter));
        ActorRef Pifagor    = system.actorOf(Philosopher.mkProps("Pifagor", waiter));
        ActorRef Platon     = system.actorOf(Philosopher.mkProps("Platon", waiter));
        ActorRef Ptolemy    = system.actorOf(Philosopher.mkProps("Ptolemy", waiter));
    }
}
