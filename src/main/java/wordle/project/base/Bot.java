package wordle.project.base;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
//import org.json.*;

class Triple {
    public String word;
    public double frequency;
    public double entropy;

    Triple(String word, double frequency,double entropy) {
        this.word=word;
        this.frequency=frequency;
        this.entropy=entropy;
    }
}
class Pair implements Comparable<Pair> {
    public String word;
    public double entropy;

    Pair(String word,double entropy) {
        this.word=word;
        this.entropy=entropy;
    }

    @Override
    public int compareTo(Pair pair) {
        return Double.compare(this.entropy,pair.entropy);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nWord: "+word);
        sb.append("\tEntropy: "+entropy);
        return sb.toString();
    }
}
enum Colors {
    GREEN {
        @Override
        public String toString() {
            return "GREEN";
        }
    },
    YELLOW {
        @Override
        public String toString() {
            return "YELLOW";
        }
    },
    GRAY {
        @Override
        public String toString() {
            return "GRAY";
        }
    };
}
class Result {
    String guess;
    Colors[] output = new Colors[5];
    Result(String guess, Colors[] output) {
        this.guess=guess;
        this.output=output;
    }
}
class WordAndFrequency {
    public String word;
    public int frequency;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word: "+word);
        sb.append("Frequency: "+frequency);
        return sb.toString();
    }

    WordAndFrequency(String word, int frequency) {
        this.word=word;
        this.frequency=frequency;
    }
}

public class Bot {
    public static List<String> words = new ArrayList<>();
    public static List<Double> frequencies = new ArrayList<>();
    public static List<Double> entropy = new ArrayList<>();
    public static List<Triple> list = new ArrayList<>();
    public static HashMap<String, Double> frequencyMap = new HashMap<>();
    public static List<String> possibleSolutions = new ArrayList<>();
    public static int wordSize = 5;
    public static List<String> copyPossibleSolutions;
    public static boolean initFlag=false;
    //public static int[] checked = new int[wordSize];
    int[] colorArray = {0, 1, 2};

    public static String translate(int num) {
        if (num == 0) return "b";
        if (num == 1) return "y";
        return "g";
    }

    public static int translate(char ch) {
        if (ch == 'b') return 0;
        if (ch == 'y') return 1;
        return 2;
    }

    public static Colors[] returnColors(String solution, String guess) {
        //List<String> wordleWordsList = Arrays.asList(wordleString.split(""));
        String[] userWordsArray = guess.split("");
        List<Boolean> wordMatchesList = new ArrayList<>();
        String checked_chars = "";// letters checked until that instant
        Colors[] output = new Colors[5];

        for (int i = 0; i < 5; i++) // checking for each letter in the word inputted and then updating the color of the panel text for each letter
        {
            checked_chars = checked_chars + userWordsArray[i];
//			System.out.println("count of " + userWordsArray[i] + " is " + count_check(guess, userWordsArray[i]) );
            if (solution.charAt(i) == guess.charAt(i)) {
                output[i] = Colors.GREEN;
            } else if (count_check(solution, userWordsArray[i]) >= count_check(checked_chars, userWordsArray[i])) {
                output[i] = Colors.YELLOW;
            } else {
                output[i] = Colors.GRAY;
            }
        }
        return output;
    }

    public static int count_check(String s, String c) {
        int count = 0;
        String[] s_arr = s.split("");
        for (int i = 0; i < s.length(); i++) {
            if (c.equals(s_arr[i])) {
                count++;
            }
        }
        return count;
    }

    //number of times c is present in the string
    public static int count_check(String s, char c) {
        int count = 0;
        String[] s_arr = s.split("");
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    // number of times ch is guessed correctly
    public static int correctGuess(String answer, String guess, char ch) {
        int count = 0;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i) && answer.charAt(i) == ch)
                count++;
        }
        return count;
    }

    //number of yellows for ch
    public static int expectedYellow(int[] checked, String answer, String guess, char ch) {
        int count = 0;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == ch && guess.charAt(i) != ch)
                count++;
        }
        return count;
    }

    //return n where index is the nth occurence of ch in guess
    public static int place(String guess, int index, char ch) {
        int count = 0;
        for (int i = 0; i <= index; i++) {
//            if(checked[i]==1)
//                continue;
            if (ch == guess.charAt(i))
                count++;
        }
        return count;
    }

    //generate color string
    public static String getColorString(String answer, String guess) {
        String color = "";
        int[] checked = new int[wordSize];

        for (int i = 0; i < guess.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i)) {
                checked[i] = 1;
                color += "g";
            } else if (count_check(answer, guess.charAt(i)) == 0) {
                checked[i] = 1;
                color += "b";
            } else {
                int howMany = count_check(guess, guess.charAt(i));
                int howManyExpected = expectedYellow(checked, answer, guess, guess.charAt(i));
                int place = place(guess, i, guess.charAt(i));
                if (place <= howManyExpected) {
                    checked[i] = 1;
                    color += "y";
                } else {
                    color += "b";
                }
            }
        }
//        System.out.println(color);
//        System.out.println(color.length());
        return color;
    }

    //    private static List<WordAndFrequency> readJson(String path) throws IOException {
//        List<WordAndFrequency> list = new ArrayList<>();
//        String lines = "";
//        try {
//            lines+=(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
//            lines+="\n";
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return list;
//    }
    public static int count = 0;

    //public static HashMap<Integer,String> pattern = new HashMap<>();
//    public static void generateHelper() {
//        HashMap<Integer,String> pattern = new HashMap<>();
//        generate(pattern,"");
//    }
//    public static void generate(HashMap<Integer,String> pattern,String str) {
//        if (wordSize == str.length()) {
//            pattern.put(count,str);
//            count++;
//        }
//        generate(pattern,str+"0");
//        generate(pattern,str+"1");
//        generate(pattern,str+"2");
//    }
    //for a string with wildcards, generate all possiible patterns
    public static HashMap<Integer, String> generatePatternsHelper(String patternString) {
        HashMap<Integer, String> pattern = new HashMap<>();
        class Inner {
            public void generatePatterns(String patternString, int i) {
                if (i >= wordSize) {
                    pattern.put(generateId(patternString), patternString);
                } else if (patternString.charAt(i) != '_') {
                    generatePatterns(patternString, i + 1);
                } else {
                    generatePatterns(replaceCharacterAtIndex(patternString, i, 'b'), i + 1);
                    generatePatterns(replaceCharacterAtIndex(patternString, i, 'y'), i + 1);
                    generatePatterns(replaceCharacterAtIndex(patternString, i, 'g'), i + 1);
                }
            }
        }
        new Inner().generatePatterns(patternString, 0);
        //System.out.println(pattern.size());
        return pattern;
    }

    //generate all possible patterns(243)
    public static HashMap<Integer, String> createPattern() {
        return generatePatternsHelper("_____");
    }

    public static int generateId(String patternString) {
        int id = 0;
        for (int i = 0; i < wordSize; i++) {
            id += translate(patternString.charAt(i)) * Math.pow(3, wordSize - i - 1);
        }
        return id;
    }

    public static String replaceCharacterAtIndex(String str, int index, char ch) {
        return (str.substring(0, index) + ch + str.substring(index + 1));
    }
    //filtering words that match the
    /* answer-> speed
    pattern-> bbggb
    shortlist solutions such that colorString(answer,guess) = colorString(check,guess)
    */

    public static List<String> getWordsThatFitPattern(List<String> words, String answer, String patternString, String guess) {
        return words.stream().filter(word -> wordFitsPattern(answer, patternString, word)).collect(Collectors.toList());
    }

    public static boolean wordFitsPattern(String answer, String patternString, String guess, String testingWord) {
        String color = getColorString(answer, guess);
        for (int i = 0; i < wordSize; i++) {
            if (patternString.charAt(i) == '_')
                continue;
            if (patternString.charAt(i) != color.charAt(i))
                return false;
        }
        return true;
    }

    public static boolean wordFitsPattern(String answer, String patternString, String testingWord) {
        String color = getColorString(answer, testingWord);
        for (int i = 0; i < wordSize; i++) {
            if (patternString.charAt(i) == '_')
                continue;
            if (patternString.charAt(i) != color.charAt(i))
                return false;
        }
        return true;
    }

    public static boolean tryWordFitsPattern(String guess, String patternString, String check) {
        String color = getColorString(check, guess);
        for (int i = 0; i < wordSize; i++) {
            if (patternString.charAt(i) == '_')
                continue;
            if (patternString.charAt(i) != color.charAt(i))
                return false;
        }
        return true;
    }

    public static List<String> getWordsThatFitPattern(List<String> list, String guess, String patternString) {
        return list.stream().filter(testingWord -> wordFitsPattern(guess, patternString, testingWord)).collect(Collectors.toList());
    }

    //shortlist pS to words that generate that color String
    public static List<String> getWordsThatFitPattern(String guess, String patternString) {
        return possibleSolutions.stream().filter(check -> tryWordFitsPattern(guess, patternString, check)).collect(Collectors.toList());
    }

    //generate list of words that generate the same colorString with potentialGuess as answer
    public static List<String> generateList(String answer, String guess, String patternString) {
        return getWordsThatFitPattern(new ArrayList<>(possibleSolutions), guess, patternString);
    }

    public static int getBinSize() {
        return (int) Math.pow(3, wordSize);
    }

    public static HashMap<String, Double> calculateEntropyForCurrentPattern(String answer, String patternString, String guess) {
        double totalFrequency = 0;
        possibleSolutions = getWordsThatFitPattern(guess, patternString);
        HashMap<String, Double> entropyMap = new HashMap<>();
        for (String possibleSolution : possibleSolutions) {
            double currentFrequency = frequencyMap.get(possibleSolution);
            totalFrequency += currentFrequency;
        }
        for (int i = 0; i < words.size(); i++) {
            double currentFrequency = frequencyMap.get(words.get(i));
            double p = sigmoid((1.0 * currentFrequency) / totalFrequency);
            double calc = -1 * p * Math.log(p) / Math.log(2);
            double currentEntropy = ((1.0 * calc) / totalFrequency);
            entropyMap.put(words.get(i), currentEntropy);
        }
        return entropyMap;
    }

    public static HashMap<String, Double> calculateEntropyForCurrentPattern(List<String> words, String answer, String patternString) {
        double totalFrequency = 0;
        HashMap<String, Double> entropyMap = new HashMap<>();
        for (String word : words) {
            double currentFrequency = frequencyMap.get(word);
            totalFrequency += currentFrequency;
        }
        for (int i = 0; i < words.size(); i++) {
            double currentFrequency = frequencyMap.get(words.get(i));
            double p = sigmoid((1.0 * currentFrequency) / totalFrequency);
            double calc = -1 * p * Math.log(p) / Math.log(2);
            double currentEntropy = ((1.0 * currentFrequency) / totalFrequency);
            entropyMap.put(words.get(i), currentEntropy);
        }
        return entropyMap;
    }

    public static HashMap<String, Double> calculateEntropyForPattern(List<String> words, String answer, String patternString, String guess) {
        words = getWordsThatFitPattern(words, answer, patternString);
        HashMap<Integer, String> patterns = generatePatternsHelper(patternString);
        HashMap<String, Double> entropyMap = new HashMap<>();

        // Iterating every set of entry in the HashMap
        for (Map.Entry<Integer, String> pattern : patterns.entrySet()) {
            String currentPattern = pattern.getValue();
            List<String> currentWords = getWordsThatFitPattern(words, answer, patternString);
            HashMap<String, Double> currentEntropyMap = calculateEntropyForCurrentPattern(currentWords, answer, currentPattern);

            for (Map.Entry<String, Double> word : currentEntropyMap.entrySet()) {
                String currentWord = word.getKey();
                Double currentEntropy = word.getValue();
                if (entropyMap.containsKey(currentWord))
                    entropyMap.put(currentWord, entropyMap.get(currentWord) + currentEntropy);
                else
                    entropyMap.put(currentWord, currentEntropy);
            }
        }
        return entropyMap;
    }

    public static HashMap<String, Double> calculateEntropyForPattern(List<String> words, String answer, String patternString) {
        words = getWordsThatFitPattern(words, answer, patternString);
        HashMap<Integer, String> patterns = generatePatternsHelper(patternString);
        HashMap<String, Double> entropyMap = new HashMap<>();

        // Iterating every set of entry in the HashMap
        for (Map.Entry<Integer, String> pattern : patterns.entrySet()) {
            String currentPattern = pattern.getValue();
            List<String> currentWords = getWordsThatFitPattern(words, answer, patternString);
            HashMap<String, Double> currentEntropyMap = calculateEntropyForCurrentPattern(currentWords, answer, currentPattern);

            for (Map.Entry<String, Double> word : currentEntropyMap.entrySet()) {
                String currentWord = word.getKey();
                Double currentEntropy = word.getValue();
                if (entropyMap.containsKey(currentWord))
                    entropyMap.put(currentWord, entropyMap.get(currentWord) + currentEntropy);
                else
                    entropyMap.put(currentWord, currentEntropy);
            }
        }
        return entropyMap;
    }

    public static List<Pair> convertHashMapToList(HashMap<String, Double> entropyMap) {
        List<Pair> list = new ArrayList<>();
        for (Map.Entry<String, Double> current : entropyMap.entrySet()) {
            String currentWord = current.getKey();
            Double currentEntropy = current.getValue();
            list.add(new Pair(currentWord, currentEntropy));
        }
        return list;
    }

    public static String guess(HashMap<String, Double> entropyMap, List<String> words, String answer, String patternString) {
        if ("_____".equals(patternString))
            return "salet";
        entropyMap = calculateEntropyForPattern(words, answer, patternString);
        List<Pair> list = convertHashMapToList(entropyMap);
//        Collections.sort(list);
//        int size=list.size();
//        System.out.println(list.get(0).word+" "+list.get(0).entropy);
//        System.out.println(list.get(size-1).word+" "+list.get(size-1).entropy);
        return Collections.max(list).word;
    }

    //generate entropy map for this potential guess from possibleSolutions
    public static double generateEntropy(String answer, String potentialGuess, String patternString, double totalFrequency) {
        List<String> wordsForThisGuess = generateList(answer, potentialGuess, patternString);
        HashMap<String, Double> entropyMap = new HashMap<>();
        double frequencyForWords = 0;
        for (String currentWord : wordsForThisGuess) {
            frequencyForWords += frequencyMap.get(currentWord);
        }
        double p = sigmoid((1.0 * frequencyForWords) / totalFrequency);
        double calc = -1 * p * Math.log(p) / Math.log(2);
        double currentEntropy = ((1.0 * calc));
        return currentEntropy;
    }

    public static double computeEntropy(double[] bins) {
        double total = 0;
        double entropy = 0;
        for (int i = 0; i < bins.length; i++)
            total += bins[i];
        //  System.out.println("Total: "+total);
        if (total == 0)
            return entropy;
        for (int i = 0; i < bins.length; i++) {
            if (bins[i] == 0)
                continue;
            double p = sigmoid(bins[i] / total);
            double calc = -1 * p * Math.log(p) / Math.log(2);
            entropy += calc;
//            System.out.println("i: "+i);
//            System.out.println("bins[i]: "+bins[i]);
//            System.out.println("p: "+p);
//            System.out.println("calc: "+calc);
//            System.out.println("entropy: "+entropy);
        }
        return entropy;
    }

    public static String guess() {
        double maxEntropy = -1;
        String bestGuess = "";

        for (String potentialGuess : words) {
            double[] bins = new double[getBinSize()];
            boolean flag = false;
            for (String potentialAnswer : possibleSolutions) {
                int id = generateId(getColorString(potentialAnswer, potentialGuess));
                if (frequencyMap.containsKey(potentialAnswer))
                    flag = true;
                bins[id] += frequencyMap.get(potentialAnswer);
            }
            if (!flag)
                continue;
            double currentEntropy = computeEntropy(bins);
            if (currentEntropy > maxEntropy) {
                maxEntropy = currentEntropy;
                bestGuess = potentialGuess;
//                System.out.println("New Max: " + maxEntropy);
//                System.out.println("New best: " + bestGuess);
            }
        }
        System.out.println("New best: " + bestGuess);
        return bestGuess;
    }
    public static String guess(List<String> solutions) {
        double maxEntropy = -1;
        String bestGuess = "";

        for (String potentialGuess : words) {
            double[] bins = new double[getBinSize()];
            boolean flag = false;
            for (String potentialAnswer : solutions) {
                int id = generateId(getColorString(potentialAnswer, potentialGuess));
                if (frequencyMap.containsKey(potentialAnswer))
                    flag = true;
                bins[id] += frequencyMap.get(potentialAnswer);
            }
            if (!flag)
                continue;
            double currentEntropy = computeEntropy(bins);
            if (currentEntropy > maxEntropy) {
                maxEntropy = currentEntropy;
                bestGuess = potentialGuess;
//                System.out.println("New Max: " + maxEntropy);
//                System.out.println("New best: " + bestGuess);
            }
        }
        System.out.println("New best: " + bestGuess);
        return bestGuess;
    }


    public static void testBins() {
        String potentialGuess = "salet";
        double[] bins = new double[getBinSize()];
        for (String potentialAnswer : possibleSolutions) {
            int id = generateId(getColorString(potentialAnswer, potentialGuess));
            bins[id] += frequencyMap.get(potentialAnswer);
            // System.out.println("Bins["+id+"]="+bins[id]);
        }
        for (int i = 0; i < bins.length; i++)
            System.out.println("Bins[" + i + "] = " + bins[i]);
        double currentEntropy = computeEntropy(bins);
        System.out.println("Entropy: " + currentEntropy);
    }

//    public static void play(String answer) {
//        HashMap<String,Double> entropyMap = new HashMap<>();
//        String patternString="_____";
//        for(int i=0;i<10000;i++) {
//            String guess=guess(entropyMap,words,answer,patternString);
//            System.out.println(i+": "+guess);
//            if(guess.equals(answer))
//                break;
//            patternString=getColorString(answer,guess);
//            System.out.println(i+": "+patternString);
//            entropyMap = calculateEntropyForPattern(words,answer,patternString);
//        }
//    }
//    public static void calculateAllEntropy() {
//        int totalFrequency=0;
//        int size=words.size();
//        for (int i = 0; i < size; i++) {
//            double value = frequencies.get(i);
//            totalFrequency++;
//        }
//        for (int i = 0; i < words.size(); i++) {
//            double freq = frequencies.get(i);
//            double p = (1.0 * freq)/ totalFrequency;
//            if (freq > 0) {
//                double calc = -1 * p * Math.log(p) / Math.log(2);
//                entropy.add(calc);
//                list.add(new Triple(words.get(i),frequencies.get(i),calc));
//            }
//
//        }
//      //  return entropy;
//    }

    public static void calculateEntropyOfWordOverPattern() {

    }

    //    public
//    public void calculate_entropies(possible_words, pattern_dict, all_patterns) {
//
//    }
    public static double sigmoid(double x) {
        return 1 / (1 + (Math.pow(Math.E, -1 * x)));
    }

    public static List<Pair> entropyMapForAnswer(String answer) {
        return convertHashMapToList(calculateEntropyForPattern(words, answer, "_____"));
    }

    //    public static HashMap<String,Double> entropyMapForAnswer(String answer, boolean) {
//        return convertHashMapToList(calculateEntropyForPattern(words,answer,"_____"));
//    }
    public static void entropyMapForAnswer(HashMap<String, Double> currentMap, String answer) {
        HashMap<String, Double> entropyMap = calculateEntropyForPattern(words, answer, "_____");
        for (Map.Entry<String, Double> current : entropyMap.entrySet()) {
            String currentWord = current.getKey();
            Double currentEntropy = current.getValue();
            if (currentMap.containsKey(currentWord))
                currentMap.put(currentWord, currentMap.get(currentWord) + currentEntropy);
            else
                currentMap.put(currentWord, currentEntropy);
        }
    }

    public static HashMap<String, Double> averageEntropy(List<String> words) {
        String word = words.get(0);
        HashMap<String, Double> entropyMap = new HashMap<>();
        entropyMapForAnswer(entropyMap, word);
        for (int i = 1; i < 1000; i++) {
            System.out.println(i);
            entropyMapForAnswer(entropyMap, words.get(i));
        }
        int numberOfWords = words.size();
        for (Map.Entry<String, Double> current : entropyMap.entrySet()) {
            String currentWord = current.getKey();
            Double currentEntropy = current.getValue();
            currentEntropy /= numberOfWords;
            entropyMap.put(currentWord, currentEntropy);
        }
        return entropyMap;
    }
    //    public String nextGuess(List<String> words, String answer,String guess, String patternString) {
//
//    }
    //public static String nextGuess()
    public static void init() {
        try {
            File file = new File("./assets/word_list.txt");
            //    File file=new File("C:\\Users\\Kalyan\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\word_list.txt");    //creates a new file instance
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
            String line;
            double totalFrequency = 0;
            while ((line = br.readLine()) != null) {
                String[] line_arr = line.split(":");
                String currentWord = line_arr[0];
                double currentFrequency = Double.parseDouble(line_arr[1]);
                if (currentFrequency <= 0)
                    continue;
                words.add(line_arr[0]);
                frequencies.add(Double.parseDouble(line_arr[1]));
                frequencyMap.put(currentWord, currentFrequency * Math.pow(10, 8));
                totalFrequency += currentFrequency;
            }
            fr.close();
//            for(int i=0;i<words.size();i++) {
//                frequencyMap.put(words.get(i),frequencies.get(i)*totalFrequency);
//            }
            //  System.out.println("Contents of File: ");
//            for (int i=0; i < words.size(); i++)
//            {
//                System.out.println(words.get(i) +" --- "+frequencies.get(i));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File file = new File("./assets/valid_solutions.txt");
            //    File file=new File("C:\\Users\\Kalyan\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\word_list.txt");    //creates a new file instance
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
            String line;

            while ((line = br.readLine()) != null) {
                String[] line_arr = line.split(":");
                String currentWord = line_arr[0];
                //double currentFrequency=Double.parseDouble(line_arr[1]);
//                if(currentFrequency<=0)
//                    continue;
                possibleSolutions.add(line_arr[0]);
//                frequencies.add(Double.parseDouble(line_arr[1]));
//                frequencyMap.put(currentWord,currentFrequency);
            }
            fr.close();
            //  System.out.println("Contents of File: ");
//            for (int i=0; i < words.size(); i++)
//            {
//                System.out.println(words.get(i) +" --- "+frequencies.get(i));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        copyPossibleSolutions = new ArrayList<>(possibleSolutions);
        initFlag=true;
//       calculateAllEntropy();
//        int size=words.size();
//        generateHelper();
//        System.out.println(entropy.size());
//        System.out.println(words.size());
//        for(int i=0;i<list.size();i++) {
//            System.out.println("Word: "+list.get(i).word+"\t Entropy: "+list.get(i).entropy);
//        }
        //  test("speed","eeere");
        //System.out.println(wordFitsPattern("speed","yybbb","eerie"));
//        List<String> possibles = getWordsThatFitPattern(words,"speed","__gg_");
//        for(String word: possibles)
//            System.out.println(word);
//        HashMap<String,Double> entropyMap = calculateEntropyForPattern(words,"speed","_____");
//        TreeMap<String, Double> sorted = new TreeMap<>();
//        sorted.putAll(entropyMap);
//        //System.out.println(sorted);
//        SortedSet<Double> values = new TreeSet<>(entropyMap.values());
//        System.out.println(values);

//        List<Pair> list = convertHashMapToList(calculateEntropyForPattern(words,"speed","_____"));
//        Collections.sort(list);
//        System.out.println(list);
//        List<Pair> list = convertHashMapToList(averageEntropy(words));
//        Collections.sort(list);
//        System.out.println(list);
        //  play("abase");
        // testBins();
    }
    public static void reset() {
        possibleSolutions.removeAll(possibleSolutions);
        for(String word: copyPossibleSolutions) {
            possibleSolutions.add(word);
        }
    }
    public static void driver() {
        if(!initFlag)
            init();
        List<String> cycle = new ArrayList<>(possibleSolutions);
        List<String> pos = new ArrayList<>(possibleSolutions);
        for (int i=0;i<200;i++) {
            int randomIndex= new Random().nextInt(cycle.size());
            String answer=cycle.get(randomIndex);
            if (new WordleGame().play(answer) == 0) {
                System.out.println(answer);
                break;
            }
            possibleSolutions.removeAll(possibleSolutions);
            for(String word: pos) {
                possibleSolutions.add(word);
            }
        }
    }
    public static String choose() {
        if(!initFlag)
            init();
        int size = possibleSolutions.size();
        return possibleSolutions.get(new Random().nextInt(size));
    }

    public static void main(String[] args) throws IOException {
        // String solution="chair";
//        Result[] guesses = new Result[6];
//      //  int count=0;
//
//        try {
//            File file = new File("./assets/word_list.txt");
//            //    File file=new File("C:\\Users\\Kalyan\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\word_list.txt");    //creates a new file instance
//            FileReader fr = new FileReader(file);   //reads the file
//            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
//            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
//            String line;
//            double totalFrequency = 0;
//            while ((line = br.readLine()) != null) {
//                String[] line_arr = line.split(":");
//                String currentWord = line_arr[0];
//                double currentFrequency = Double.parseDouble(line_arr[1]);
//                if (currentFrequency <= 0)
//                    continue;
//                words.add(line_arr[0]);
//                frequencies.add(Double.parseDouble(line_arr[1]));
//                frequencyMap.put(currentWord, currentFrequency * Math.pow(10, 8));
//                totalFrequency += currentFrequency;
//            }
//            fr.close();
////            for(int i=0;i<words.size();i++) {
////                frequencyMap.put(words.get(i),frequencies.get(i)*totalFrequency);
////            }
//            //  System.out.println("Contents of File: ");
////            for (int i=0; i < words.size(); i++)
////            {
////                System.out.println(words.get(i) +" --- "+frequencies.get(i));
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            File file = new File("./assets/valid_solutions.txt");
//            //    File file=new File("C:\\Users\\Kalyan\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\word_list.txt");    //creates a new file instance
//            FileReader fr = new FileReader(file);   //reads the file
//            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
//            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                String[] line_arr = line.split(":");
//                String currentWord = line_arr[0];
//                //double currentFrequency=Double.parseDouble(line_arr[1]);
////                if(currentFrequency<=0)
////                    continue;
//                possibleSolutions.add(line_arr[0]);
////                frequencies.add(Double.parseDouble(line_arr[1]));
////                frequencyMap.put(currentWord,currentFrequency);
//            }
//            fr.close();
//            //  System.out.println("Contents of File: ");
////            for (int i=0; i < words.size(); i++)
////            {
////                System.out.println(words.get(i) +" --- "+frequencies.get(i));
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////       calculateAllEntropy();
////        int size=words.size();
////        generateHelper();
////        System.out.println(entropy.size());
////        System.out.println(words.size());
////        for(int i=0;i<list.size();i++) {
////            System.out.println("Word: "+list.get(i).word+"\t Entropy: "+list.get(i).entropy);
////        }
//        //  test("speed","eeere");
//        //System.out.println(wordFitsPattern("speed","yybbb","eerie"));
////        List<String> possibles = getWordsThatFitPattern(words,"speed","__gg_");
////        for(String word: possibles)
////            System.out.println(word);
////        HashMap<String,Double> entropyMap = calculateEntropyForPattern(words,"speed","_____");
////        TreeMap<String, Double> sorted = new TreeMap<>();
////        sorted.putAll(entropyMap);
////        //System.out.println(sorted);
////        SortedSet<Double> values = new TreeSet<>(entropyMap.values());
////        System.out.println(values);
//
////        List<Pair> list = convertHashMapToList(calculateEntropyForPattern(words,"speed","_____"));
////        Collections.sort(list);
////        System.out.println(list);
////        List<Pair> list = convertHashMapToList(averageEntropy(words));
////        Collections.sort(list);
////        System.out.println(list);
//       //  play("abase");
//        // testBins();
//        List<String> cycle = new ArrayList<>(possibleSolutions);
//        List<String> pos = new ArrayList<>(possibleSolutions);
//        for (int i=0;i<200;i++) {
//            int randomIndex= new Random().nextInt(cycle.size());
//            String answer=cycle.get(randomIndex);
//            if (play(answer) == 0) {
//                System.out.println(answer);
//                break;
//            }
//            possibleSolutions.removeAll(possibleSolutions);
//            for(String word: pos) {
//                possibleSolutions.add(word);
//            }
//        }
        driver();
    }
}