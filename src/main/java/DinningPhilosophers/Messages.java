package DinningPhilosophers;


public class Messages {
    private Messages() {}

    public static class Introduce {
        private String mPhilosopher;

        public Introduce(String philosopherName) {
            mPhilosopher = philosopherName;
        }

        public String getPhilosopherName() {
            return mPhilosopher;
        }
    }

    public static class Think {}
    public static class Eat { }
    public static class FinishEat { }
    public static class Hungry { }

}
