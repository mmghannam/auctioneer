import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Benchmarks {

    public static Hashtable<String, Double> getAveragePerfomance(List<String> strategyNames, double[] res) {
        Hashtable<String, ArrayList<Double>> strategies_results = new Hashtable<>();
        for (int i = 0; i != strategyNames.size(); ++i) {
            if (!strategyNames.get(i).contains("Bidder")) {
                System.out.println(strategyNames.get(i));
            }
            String strategy_name = strategyNames.get(i).substring(7);
            if (!strategies_results.containsKey(strategy_name)) {
                ArrayList<Double> strategy_results = new ArrayList<>();
                strategy_results.add(res[i]);
                strategies_results.put(strategy_name, strategy_results);

            } else {
                strategies_results.get(strategy_name).add(res[i]);
            }
        }

        Hashtable<String, Double> averages = new Hashtable<>();

        Set<String> keys = strategies_results.keySet();
        for (String strategy_name : keys) {
            ArrayList<Double> strategy_results = strategies_results.get(strategy_name);
            double sum = 0;
            for (double result : strategy_results) {
                sum += result;
            }
            averages.put(strategy_name, sum / strategy_results.size());
        }
        return averages;
    }


    public static void printScores(List<String> strategyNames, double[] res, Integer N) {
        javafx.util.Pair<String, Double> instanceResults[] = new javafx.util.Pair[strategyNames.size()];
        for (int i = 0; i < strategyNames.size(); i++) {
            instanceResults[i] = new javafx.util.Pair<>(strategyNames.get(i), res[i]);
        }
        Arrays.sort(instanceResults, (p1, p2) -> p2.getValue().compareTo(p1.getValue()));
        System.out.println("netID, score");
        for (int i = 0; i < N; ++i) {
            System.out.println(instanceResults[i].getKey().substring(7) + ": " + Double.toString(instanceResults[i].getValue()));
        }
    }

    public static List<String> loadStrategies(String fileName) throws FileNotFoundException {
        final BufferedReader namesFile = new BufferedReader(new FileReader(fileName));
        final List<String> strategyNames =
            namesFile.lines().map(s -> String.format("Bidder_%s", s)).collect(Collectors.toList());
        return strategyNames;
    }

    public static double[] getResults(List<String> strategyNames, int numTrials) {
        final Auctioneer withStrategies = new Auctioneer(strategyNames);
        return withStrategies.oneEachTrials(numTrials, Auctioneer.defaultConfig);
    }

    public static List<String> generateStrategyList(HashMap<String, Integer> strategyCounts) {
        List<String> strategyNames = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : strategyCounts.entrySet()) {
            String strategy = entry.getKey();
            Integer count = entry.getValue();
            for (int i = 0; i < count; i++) {
                strategyNames.add(String.format("Bidder_%s", strategy));
            }
        }
        return strategyNames;
    }

    public static void nOnNTest(String[] strategies, int numTrials, int startingCount) {
        double score;
        System.out.println("\n###" + startingCount + " on " + startingCount + " Test:\n###Strategy, score");
        for (String testedStrategy : strategies) {
            score = 0;
            for (String oppositeStrategy : strategies) {
                if (!testedStrategy.equals(oppositeStrategy)) {
                    List<String> strategyNames = generateStrategyList(new HashMap<String, Integer>() {{
                        put(testedStrategy, startingCount);
                        put(oppositeStrategy, startingCount);
                    }});
                    double[] results = getResults(strategyNames, numTrials);
                    Hashtable<String, Double> averages = getAveragePerfomance(strategyNames, results);
                    score += averages.get(testedStrategy) - averages.get(oppositeStrategy);
                }
            }
            System.out.println(testedStrategy + ", " + score);
        }
    }

    public static void homogenousTest(String[] strategies, int numTrials) {
        System.out.println("\n###Homogeneous Test:\n###Strategy, score");
        for (String testedStrategy : strategies) {
            List<String> strategyNames = generateStrategyList(new HashMap<String, Integer>() {{
                put(testedStrategy, 50);
            }});
            double[] results = getResults(strategyNames, numTrials);
            Hashtable<String, Double> averages = getAveragePerfomance(strategyNames, results);
            System.out.println(testedStrategy + ", " + averages.get(testedStrategy));
        }
    }

    public static void allCombinationsTest(String[] strategies, int numTrials) {
        List<String> strategiesList = Arrays.asList(strategies);
        //loop over sublists
        Hashtable<Integer, Hashtable<String, Double>> scores_for_size = new Hashtable<>();
        Hashtable<String, Double> scores;
        List<List<String>> sublists = getAllSubLists(strategiesList);
        for (List<String> sublist : sublists) {
            if (sublist.size() == 1) continue;
            if (scores_for_size.containsKey(sublist.size())) {
                scores = scores_for_size.get(sublist.size());
            } else {
                scores = new Hashtable<>();
                scores_for_size.put(sublist.size(), scores);
            }
            List<String> strategyNames = new ArrayList<>();
            for (String strategy : sublist) {
                strategyNames.addAll(generateStrategyList(new HashMap<String, Integer>() {{
                    put(strategy, 10);
                }}));
            }
            sublist = strategyNames;
            double[] results = getResults(sublist, numTrials);

            Hashtable<String, Double> averages = getAveragePerfomance(sublist, results);
            for (String strategy : averages.keySet()) {
                if (scores.containsKey(strategy)) {
                    scores.put(strategy, scores.get(strategy) + averages.get(strategy));
                } else {
                    scores.put(strategy, averages.get(strategy));
                }
            }
        }
        for (int i = 2; i <= strategies.length; i++) {
            System.out.println("\nTotal score for sublists of size " + i);
            scores = scores_for_size.get(i);
            for (String strategy : scores.keySet()) {
                System.out.println(strategy + " " + scores.get(strategy));
            }
        }
    }

    public static List<List<String>> getSublists(List<String> originalList, int size) {
        List<List<String>> result = new LinkedList<>();

        for (int i = 0; i < originalList.size() - size + 1; i++) {
            List<String> current_list = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                current_list.add(originalList.get(i + j));
            }
            result.add(current_list);
        }
        return result;
    }

    public static <T> List<List<T>> getAllSubLists(List<T> list) {
        List<T> subList;
        List<List<T>> res = new ArrayList<>();
        List<List<Integer>> indexes = allSubListIndexes(list.size());
        for (List<Integer> subListIndexes : indexes) {
            subList = new ArrayList<>();
            for (int index : subListIndexes)
                subList.add(list.get(index));
            res.add(subList);
        }
        return res;
    }

    public static List<List<Integer>> allSubListIndexes(int n) {
        List<List<Integer>> res = new ArrayList<>();
        int allMasks = (1 << n);
        for (int i = 1; i < allMasks; i++) {
            res.add(new ArrayList<>());
            for (int j = 0; j < n; j++)
                if ((i & (1 << j)) > 0)
                    res.get(i - 1).add(j);

        }
        return res;
    }


    public static void survivalOfTheFittestTest(String[] strategies, int start_count, double worst_percent,
                                                 int numTrials) {

        //initialize params
        int starting_number_for_each_strategy = start_count;
        int population_size = starting_number_for_each_strategy * strategies.length;
        int number_to_discard = (int) (worst_percent * population_size);
        int currentGeneration = 1;

        //initialize population
        HashMap<String, Integer> population = new HashMap<String, Integer>();
        for (String strategy : strategies) {
            population.put(strategy, starting_number_for_each_strategy);

        }

        //initialize survival count
        Hashtable<String, Integer> generationsSurvived = new Hashtable<>();

        while (population_size > 10) {
            List<String> strategyNames = generateStrategyList(population);
            double[] results = getResults(strategyNames, numTrials);

            javafx.util.Pair<String, Double> instanceResults[] = sortedinstanceResults(strategyNames, results);
            discardTheWeak(instanceResults, number_to_discard, currentGeneration, population, generationsSurvived);
            currentGeneration++;
            int new_number_to_discard = (int) (worst_percent * (strategyNames.size() - number_to_discard));
            number_to_discard = new_number_to_discard == 0 ? 1 : new_number_to_discard;


            population_size = getPopulationSize(population);
            System.out.println(population_size);
        }
        System.out.println("\n###Survival of the Fittest Test:\nstrategy, generations:");
        printGenerationsSurvived(generationsSurvived);
    }

    public static int getPopulationSize(HashMap<String, Integer> population) {
        int sum = 0;
        for (int value : population.values()) {
            sum += value;
        }
        return sum;
    }

    public static void printGenerationsSurvived(Hashtable<String, Integer> generationsSurvived) {
        List<String> strategies = new ArrayList<>(generationsSurvived.keySet());
        strategies.sort((s1, s2) -> generationsSurvived.get(s2).compareTo(generationsSurvived.get(s1)));
        for (String name : strategies) {
            System.out.println(name + ", " + generationsSurvived.get(name));
        }
    }

    public static int discardTheWeak(javafx.util.Pair<String, Double>[] instanceResults, int number_to_discard,
                                      int currentGeneration, HashMap<String, Integer> population,
                                      Hashtable<String, Integer> generationsSurvived) {
        int numberOfDiscarded = 0;
        // handle the part that survived
        HashSet<String> survivedStrategies = new HashSet<>();
        for (int i = 0; i < instanceResults.length - number_to_discard; i++) {
            String strategy = instanceResults[i].getKey().substring(7);
            int survivedGenerations = generationsSurvived.getOrDefault(strategy, 0);
            if (!survivedStrategies.contains(strategy)) { // make sure it wasn't increased before
                survivedGenerations++;
                generationsSurvived.put(strategy, survivedGenerations);
                survivedStrategies.add(strategy);
            }
        }

        // handle the part that will be removed
        for (int i = instanceResults.length - number_to_discard; i < instanceResults.length; i++) {
            String strategy = instanceResults[i].getKey().substring(7);
            int strategyCountInPopulation = population.getOrDefault(strategy, 0);

            if (strategyCountInPopulation == 0) continue; // skip removed strategies

            if (strategyCountInPopulation - 1 == 0) {
                population.remove(strategy); // discard the strategy when it decreases to less than 10 individuals
                numberOfDiscarded++;
            } else {
                population.put(strategy, strategyCountInPopulation - 1);  // remove 1 instance
                int survivedGenerations = generationsSurvived.getOrDefault(strategy, 0);
                if (survivedGenerations < currentGeneration) { // make sure it wasn't increased before
                    survivedGenerations++;
                }
                generationsSurvived.put(strategy, survivedGenerations);
            }
        }
        return numberOfDiscarded;
    }

    public static javafx.util.Pair<String, Double>[] sortedinstanceResults(List<String> strategyNames, double[] results) {
        javafx.util.Pair<String, Double> instanceResults[] = new javafx.util.Pair[strategyNames.size()];
        for (int i = 0; i < strategyNames.size(); i++) {
            instanceResults[i] = new javafx.util.Pair<>(strategyNames.get(i), results[i]);
        }
        Arrays.sort(instanceResults, (p1, p2) -> p2.getValue().compareTo(p1.getValue()));
        return instanceResults;
    }
}
