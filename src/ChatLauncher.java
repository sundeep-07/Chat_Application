public class ChatLauncher {

    public static void main(String[] args) {

        Thread serverThread = new Thread(() -> {
            try {
                System.out.println("[Launcher] Starting Server...");
                Server.main(null);
            } catch (Exception e) {
                System.err.println("[Launcher] Server error:");
                e.printStackTrace();
            }
        });
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(() -> {
            try {
                System.out.println("[Launcher] Starting Client...");
                Client.main(null);
            } catch (Exception e) {
                System.err.println("[Launcher] Client error:");
                e.printStackTrace();
            }
        });
        clientThread.start();
    }
}
