

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Ex3 {
    public static Map _map;
    public static Pixel2D p1;
    public static Pixel2D p2;
    public static Pixel2D[] ps; //for TSP
   public static final int OBS = Color.BLACK.getRGB(); //default value of a pixel that represent an obstacle(for shortestPath algorithms)
    private static final int UV = Color.RED.getRGB(); //default value of a pixel that represent an unvisited pixel(for shortestPath algorithms)
    private static int _color = Color.BLACK.getRGB();
    private static final int[][] size1 = new int[10][10];
    private static final int[][] size2 = new int[30][30];
    private static final int[][] size3 = new int[50][50];
    private static final int[][] size4 = new int[120][120];
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] a) {
        int[] answers = mapInput();
        setSize(answers[0]);
        setPattern(answers[1]);
        setCyclic(answers[2]);
        int algo = answers[3];
        algoInput(algo);
        setAlgo(algo);
        drawMap(_map);
    }

    public static int[] mapInput() {
        int[] answers = new int[4];
        boolean validInput = false;
        while (!validInput) {
            System.out.println("please choose size:\n" +
                    "1. Press 1 for 10X10\n" +
                    "2. Press 2 for 30X30\n" +
                    "3. Press 3 for 50X50\n" +
                    "4. press 4 for 120X120");
            int sizeChoice = scanner.nextInt();
            if (sizeChoice >= 1 && sizeChoice <= 4) {
                answers[0] = sizeChoice;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
        }
        validInput = false;
        while (!validInput) {
            System.out.println("please choose a pattern\n" + "1. Press empty pattern\n" +
                    "2. diagonal pattern\n" +
                    "3. Press 3 for obs in the middle pattern");
            int patternChoice = scanner.nextInt();
            if (patternChoice >= 1 && patternChoice <= 3) {
                answers[1] = patternChoice;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        }
        validInput = false;
        while (!validInput) {
            System.out.println("Choose 1 for cyclic map and 2 for a not cyclic map:");
            int cyclicChoice = scanner.nextInt();
            if (cyclicChoice == 1 || cyclicChoice == 2) {
                answers[2] = cyclicChoice;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter either 1 or 2.");
            }
        }
        validInput = false;
        while (!validInput) {
            System.out.println("please choose algo:\n" +
                    "1. Press 1 for shortestPath\n" +
                    "2. Press 2 for shortestPath(TSP)\n" +
                    "3. Press 3 for fill");
            int algoChoice = scanner.nextInt();
            if (algoChoice >= 1 && algoChoice <= 3) {
                answers[3] = algoChoice;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        }

        return answers;
    }

    public static void algoInput(int algo) {
        if (algo == 1) {
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.println("please choose a start point");
                    p1 = new Index2D(scanner.next());
                    System.out.println("please choose end point");
                    p2 = new Index2D(scanner.next());
                    validInput = true;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter valid coordinates.");
                    scanner.nextLine();
                }
            }
        } else if (algo == 2) {
            boolean validInput = false;
            int num = 0;
            while (!validInput) {
                try {
                    System.out.println("enter number of points for TSP");
                    num = scanner.nextInt();
                    if (num > 0) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    scanner.nextLine();
                }
            }
            ArrayList<Pixel2D> pts = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                System.out.println("choose point in " + i + "st place");
                String p = scanner.next();
                pts.add(new Index2D(p));
            }
            Pixel2D[] psArray = new Pixel2D[pts.size()];
            ps = pts.toArray(psArray);
        } else if (algo == 3) {
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.println("please choose entry point");
                    p1 = new Index2D(scanner.next());
                    System.out.println("please choose entry point color:\n"+
                            "1. press 1 for Yellow\n"+
                            "2. press 2 for Green\n"+
                            "3. press 3 for Orange\n"+
                            "4. press 4 for Pink");
                    int color = scanner.nextInt();
                    if (color >= 1 && color <= 4){
                    color(color);
                    validInput = true;}
                    else {
                        System.out.println("Invalid color choice. Please enter a number between 1 and 4");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter valid coordinates or color.");
                    scanner.nextLine();
                }
            }
        }
    }
    public static void setCyclic(int ans) {
        _map.setCyclic(ans == 1);
    }

    public static void setAlgo(int algo) {
        if (algo == 1) {
            _map.shortestPath(p1, p2, OBS);
        } else if (algo == 2) {
            _map.shortestPath(ps, OBS);
        } else if (algo == 3) {
            _map.fill(p1, _color);
        }
    }
    public static void color(int c) {
        if (c == 1) {
            _color = Color.YELLOW.getRGB();
        }
        if (c == 2) {
            _color = Color.GREEN.getRGB();
        }
        if (c == 3) {
            _color = Color.ORANGE.getRGB();
        }
        if (c == 4) {
            _color = Color.PINK.getRGB();
        }
    }

    public static void drawMap(Map m) {
        StdDraw.setScale(-0.5, m.getWidth() - 0.5);
        for (int i = 0; i < _map.getWidth(); i++) {
            for (int j = 0; j < _map.getHeight(); j++) {
                StdDraw.setPenColor(new Color(_map.getPixel(i, j)));
                double pixelSize = 0.3;
                StdDraw.filledSquare(i, j, pixelSize);
            }
        }
    }

    public static void setSize(int size) {
        if (size == 1) {
            _map = new Map(size1);
        } else if (size == 2) {
            _map = new Map(size2);
        } else if (size == 3) {
            _map = new Map(size3);
        } else if (size == 4) {
            _map = new Map(size4);

        }
    }

    public static void setPattern(int pattern) {

        if (pattern == 1) {
            for (int i = 0; i < _map.getWidth(); i++) {
                for (int j = 0; j < _map.getHeight(); j++) {
                    _map.setPixel(i, j, UV);
                }
            }
        }
        if (pattern == 2) {
            for (int i = 0; i < _map.getWidth(); i++) {
                for (int j = 0; j < _map.getHeight(); j++) {
                    if (i == j) {
                        _map.setPixel(i, j, OBS);
                    } else {
                        _map.setPixel(i, j, UV);
                    }
                }
            }

        } else if (pattern == 3) {
            int middleX = _map.getWidth() / 2;
            for (int i = 0; i < _map.getWidth(); i++) {
                for (int j = 0; j < _map.getHeight(); j++) {
                    if (i == middleX) {
                        _map.setPixel(i, j, OBS);
                    } else {
                        _map.setPixel(i, j, UV);
                    }
                }
            }

        }
    }
}


