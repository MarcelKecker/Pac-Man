public class GameLoop implements Runnable{

    private Pacman spiel;
    private volatile boolean running;

    public GameLoop(Pacman spiel) {
        this.spiel = spiel;
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        if (running) {
            return;
        }
        running = true;
        while (running) {
            spiel.tick();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
