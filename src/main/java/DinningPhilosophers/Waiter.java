package DinningPhilosophers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.Arrays;

public class Waiter extends UntypedActor {

    public static Props mkProps(int forkCount) {
        return Props.create(Waiter.class, forkCount);
    }

    private enum ForkState { FREE, USED }
    private ForkState[] mForks;
    private ArrayList<ActorRef> mPhilosophers;

    private Waiter(int forkCount) {
        mForks = new ForkState[forkCount];
        Arrays.fill(mForks, ForkState.FREE);
        mPhilosophers = new ArrayList<ActorRef>(forkCount);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Messages.Introduce) {
            String name = ((Messages.Introduce) message).getPhilosopherName();
            System.out.println(name + " joined table. Welcome!");
            mPhilosophers.add(getSender());
            getSender().tell(new Messages.Think(), getSelf());

        }  else if (message instanceof Messages.Hungry) {
            int seat = mPhilosophers.indexOf(getSender());
            if (seat == -1) {
                System.out.println("I don`t know this philosopher");
            } else {
                int leftFork = seat;
                int rightFork = (seat + 1) % mForks.length;
                if (mForks[leftFork].equals(ForkState.FREE) && mForks[rightFork].equals(ForkState.FREE)) {
                    mForks[leftFork] = ForkState.USED;
                    mForks[rightFork] = ForkState.USED;
                    getSender().tell(new Messages.Eat(), getSelf());
                } else {
                    getSender().tell(new Messages.Think(), getSelf());
                }
            }
        } else if (message instanceof Messages.FinishEat) {
            int seat = mPhilosophers.indexOf(getSender());
            int leftFork = seat;
            int rightFork = (seat + 1) % mForks.length;
            mForks[leftFork] = ForkState.FREE;
            mForks[rightFork] = ForkState.FREE;
            getSender().tell(new Messages.Think(), getSelf());
        }

    }
}
