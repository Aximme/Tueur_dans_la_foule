    package fr.miroff.FouleProject.character;

    import java.util.Random;

    public class Civil extends Personnage {
        private static final Random RAND = new Random();

        public Civil(int x, int y) {
            super(x, y);
            Object deplacement1 = this.deplacement;
            this.health = 1;
        }

        public boolean tryToEscape() {
            return RAND.nextInt(3) == 0;
        }

    }



