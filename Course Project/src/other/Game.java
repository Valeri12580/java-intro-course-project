package other;

import elements.*;
import utils.RandomNumberGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class Game {
    private Repository repository;
    private Board board;
    private Player player;
    private Player bot;
    private Player currentPlayer;
    private int counterDebug = 0;

    private BufferedReader reader;


    public Game(Player player, Player bot) {
        this.board = new Board();
        this.repository = new Repository();
        this.player = player;
        this.bot = bot;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() {

        while (this.player.getMoney() > 0 && this.bot.getMoney() > 0) {

            cycleInit();

            while (this.player.isInCycle() || this.bot.isInCycle()) {
                printStartTurnData();
                int moveNumber = Dice.generateMoveNumber(this.currentPlayer);
                if (!this.currentPlayer.isInCycle() || this.currentPlayer.getMoney() <= 0) {
                    System.out.printf("%s приключи своя цикъл или няма достатъчно средства!Изчакване на другия играч...\n", currentPlayer.getName());
                    this.currentPlayer.setCurrentPositionIndex(20);
                    this.currentPlayer = this.generateCurrentPlayer();
                    continue;
                }
                Element desiredPositionElement = null;
                try {
                    desiredPositionElement = this.board.getElementOnIndex(moveNumber);
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("%s приключи със своя цикъл!\n", this.currentPlayer.getName());
                    this.currentPlayer.setCurrentPositionIndex(20);
                    this.currentPlayer = generateCurrentPlayer();
                    continue;
                }


                Map<Integer, ? extends Element> repositoryByType = this.repository.getRepositoryByType(desiredPositionElement);

                if (desiredPositionElement instanceof Trap) {
                    executeTrap(moveNumber, (Trap) desiredPositionElement, repositoryByType);
                } else if (desiredPositionElement instanceof Invest) {
                    executeInvest(moveNumber, repositoryByType);
                } else if (desiredPositionElement instanceof Chance) {
                    executeChance(moveNumber);
                } else if (desiredPositionElement instanceof Steal) {
                    executeEvilPlan(moveNumber, (Steal) desiredPositionElement);
                } else if (desiredPositionElement instanceof PartyHard) {
                    executePartyHard(moveNumber, repositoryByType);
                }

                this.currentPlayer = generateCurrentPlayer();
            }
            postCycleProcess();
        }

        Player winner = choseWinner();
        printWinner(winner);


    }

    /**
     * method that print data that is needed in the start of the turn
     */
    private void printStartTurnData() {
        System.out.println("-".repeat(100) + ++counterDebug);
        System.out.printf("Текущ играч - %s ,\n", this.currentPlayer.getStats());
        this.board.printBoard();
    }

    /**
     * method that process cycle result
     */
    private void postCycleProcess() {
        this.processEndCycleResult(player);
        this.processEndCycleResult(bot);
        postCycleInit();
    }

    /**
     * method that reset punishments and steals after the cycle
     */
    private void postCycleInit() {
        this.player.resetPunishments();
        this.bot.resetPunishments();
        this.repository.resetSteals();
    }

    /**
     * method that do initialization work
     */
    private void cycleInit() {
        this.player.setCurrentPositionIndex(0);
        this.bot.setCurrentPositionIndex(0);

        this.board.randomizeFields();

        this.generateRandomSteal(this.player);
        this.generateRandomSteal(this.bot);

        this.setNewElements(0, player, new Start());
        this.setNewElements(0, bot, new Start());

        this.currentPlayer = this.generateCurrentPlayerAtStartingCycle();
    }

    /**
     * chooses the winner
     *
     * @return the winner
     */
    private Player choseWinner() {
        if (this.player.getMoney() <= 0 && this.bot.getMoney() <= 0) {
            return null;
        }
        return this.player.getMoney() <= 0 ? this.bot : this.player;

    }

    /**
     * generate the first player that will start with the next cycle
     *
     * @return
     */
    private Player generateCurrentPlayerAtStartingCycle() {
        if (this.currentPlayer == null) {
            int randomNumber = RandomNumberGenerator.generateRandomIntegerNumber(2);

            return randomNumber == 0 ? this.player : this.bot;

        }

        return this.player.getMoney() > this.bot.getMoney() ? this.player : this.bot;
    }

    /**
     * determines the next player
     * @return the next player
     */
    private Player generateCurrentPlayer() {
        return this.currentPlayer.equals(bot) ? this.player : this.bot;
    }

    /**
     * execute evil plan
     * @param moveNumber the future position of the current player
     * @param element element that will be replaced
     */
    private void executeEvilPlan(int moveNumber, Steal element) {
        this.currentPlayer.activateEvilPlan(element);
        this.setNewElements(moveNumber, currentPlayer, element);
    }

    /**
     * execute party hard
     * @param moveNumber  the future position of the current player
     * @param repositoryByType specific repository
     */
    private void executePartyHard(int moveNumber, Map<Integer, ? extends Element> repositoryByType) {
        printMenu(repositoryByType);
        int choice = readGeneralChoice(repositoryByType, currentPlayer);
        PartyHard chosenParty = this.repository.getParties().get(choice);
        chosenParty.activate(currentPlayer);

        this.setNewElements(moveNumber, currentPlayer, chosenParty);
    }

    /**
     * execute chance
     * @param moveNumber the future position of the current player
     */
    private void executeChance(int moveNumber) {
        String type = this.generateChanceType();
        int number = this.generateChanceNumber();
        Chance chance = this.repository.getChances().stream()
                .filter(e -> e.getType().equals(type) && (e.getMinInterval() <= number && number <= e.getMaxInterval()))
                .findFirst().orElseThrow(NoSuchElementException::new);

        chance.activate(currentPlayer);

        this.setNewElements(moveNumber, currentPlayer, chance);
    }

    /**
     *
     * @param moveNumber the future position of the current player
     * @param trap element
     * @param repositoryByType specific repository
     */
    private void executeTrap(int moveNumber, Trap trap, Map<Integer, ? extends Element> repositoryByType) {
        if (trap.getOwner() != null) {
            trap.activate(currentPlayer);
            this.setNewElements(moveNumber, currentPlayer, new Trap());
            return;
        }

        printMenu(repositoryByType);
        System.out.println("Choose option from the menu above(if you dont have money for the option the other player's turn will begin.");
        int choice = this.readGeneralChoice(repositoryByType, currentPlayer);
        if(choice==5){
            this.setNewElements(moveNumber, currentPlayer, new Trap());
            return;
        }
        Trap chosenTrap = this.repository.getCopyOfTrapFromRepositoryWithNewReference(choice);

        chosenTrap.activate(currentPlayer);


        this.setNewElements(moveNumber, currentPlayer, chosenTrap);
    }

    /**
     *
     * @param moveNumber the future position of the current player
     * @param repositoryByType specific repository
     */
    private void executeInvest(int moveNumber, Map<Integer, ? extends Element> repositoryByType) {
        int choice;
        do {
            printMenu(repositoryByType);
            System.out.println("Избери опция от менюто по-горе.");

            choice = readGeneralChoice(repositoryByType, currentPlayer);

            if (this.currentPlayer.getPlayerType().equals(PlayerType.BOT)) {
                if (RandomNumberGenerator.generateRandomIntegerNumber(2) == 0) {
                    choice = 3;
                }
            }

            if (choice == 3) {
                continue;
            }

            Invest chosenInvestment = this.repository.getRandomInvests().get(choice);

            if (currentPlayer.getMoney() < chosenInvestment.getMinimalInvestmentPrice()) {
                System.out.println("Нямате достатъчно пари,за да инвестирате,в избраната от вас компания...");
                continue;
            }

            System.out.printf("Колко пари искате да инвестирате?(минималната сума е:%d)\n", chosenInvestment.getMinimalInvestmentPrice());
            double money = readInvestPrice(chosenInvestment, this.currentPlayer);

            this.currentPlayer.invest(money, chosenInvestment);
            System.out.printf("%s ,вие инвенстирахте успешно %.2f в %s\n", currentPlayer.getName(), money, chosenInvestment.getName());
            System.out.printf("Оставащи пари:%.2f\n", this.currentPlayer.getMoney());
        } while (choice != 3);

        System.out.printf("%s прекрати инвестирането!\n", this.currentPlayer.getName());
        this.setNewElements(moveNumber, this.currentPlayer, this.board.getElementOnIndex(moveNumber));

    }

    /**
     * print menu with available options
     * @param repo specific repository from which to take options
     * @param <T> type of the elements in the repository
     */
    private <T> void printMenu(Map<Integer, T> repo) {
        repo.forEach((key, value) -> {
            try {
                Field field = value.getClass().getSuperclass().getDeclaredField("name");
                field.setAccessible(true);
                String data = field.get(value).toString();
                System.out.println(String.format("%d :%s", key, data));
            } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
        });
    }


    /**
     * reading the  choice from print menu
     * @param repositoryByType specific repo
     * @param player determines if the player is bot or not
     * @return choice
     */
    private int readGeneralChoice(Map<Integer, ? extends Element> repositoryByType, Player player) {
        int input = -1;

        if (player.getPlayerType().equals(PlayerType.BOT)) {
            input = RandomNumberGenerator.generateRandomIntegerNumber(repositoryByType.size() - 1);
            return input;
        }

        do {
            System.out.print("Избери валидна опция:");
            try {
                input = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.println("Трябва ми число!");

            }
        } while (!repositoryByType.containsKey(input));

        return input;
    }


    /**
     * takes from the input the value of the investment
     * @param invest Invest object
     * @param player current player
     * @return value of the investition
     */
    private double readInvestPrice(Invest invest, Player player) {
        double money = -1.0;

        if (player.getPlayerType().equals(PlayerType.BOT)) {
            //moje da ne raboti
            money = RandomNumberGenerator.generateRandomDoubleNumber(invest.getMinimalInvestmentPrice(), player.getMoney());

            return money;
        }

        do {
            try {
                money = Double.parseDouble(reader.readLine());
                if (money < 0) {
                    throw new IllegalArgumentException("Инвестицията не може да бъде <=0");
                }
                //edge case->ako nqma pari za minimum investiciqta
                if (money > this.currentPlayer.getMoney()) {
                    throw new IllegalArgumentException("Нямате достатъчно пари,опитайте отново!");
                }
                if (money < invest.getMinimalInvestmentPrice()) {
                    throw new IllegalArgumentException(String.format("Минималната инвестиция е: %d", invest.getMinimalInvestmentPrice()));
                }
            } catch (Exception e) {
                money = -1;
                System.out.println(e.getMessage());
            }
        } while (money == -1);

        return money;
    }


    /**
     * generate random steal
     * @param player player to be given
     */
    private void generateRandomSteal(Player player) {

        List<Steal> steals = this.repository.getSteals();

        int randomNumber = RandomNumberGenerator.generateRandomIntegerNumber(steals.size());
        player.setEvilPlan(this.repository.getSteals().get(randomNumber));
    }

    /**
     * set new position of different elements
     * @param moveNumber
     * @param currentPlayer
     * @param passiveElement
     */
    private void setNewElements(int moveNumber, Player currentPlayer, Element passiveElement) {
        this.board.setElement(moveNumber, passiveElement);

        this.board.setPlayer(moveNumber, currentPlayer.getCurrentPositionIndex(), currentPlayer);

        currentPlayer.setCurrentPositionIndex(moveNumber);


    }

    /**
     * generate positive or negative chance
     * @return string that indicates the type
     */
    private String generateChanceType() {
        int number = Dice.generateTenWallDiceNumber();
        return number % 2 == 0 ? "positive" : "negative";
    }

    /**
     * generate chance number
     * @return
     */
    private int generateChanceNumber() {
        return Dice.generateOneHundredWallDiceNumber();
    }

    /**
     * process end cycle result
     * @param player to whom the result should be calculated
     */
    private void processEndCycleResult(Player player) {
        double profitFromInvestments = this.calculateIncomeFromAllInvestments(player);
        double profitFromInvestmentsAfterPunishments = this.calculateIncomeFromInvestmentsAfterPunishments(profitFromInvestments, player);

        player.addMoney(profitFromInvestmentsAfterPunishments);
        System.out.printf("Печалбата от инвестициите на %s е:%.2f\n", player.getName(), profitFromInvestmentsAfterPunishments);
        System.out.printf("Състоянието на %s e:%.2f\n", player.getName(), player.getMoney());


    }

    /**
     * calculates the result from investments
     * @param player to whom the result should be calculated
     * @return value
     */
    private double calculateIncomeFromAllInvestments(Player player) {
        double result = 0.0;
        for (Map.Entry<Invest, Double> investDoubleEntry : player.getInvestments().entrySet()) {
            if (calculateNumberInRiskRatio(investDoubleEntry.getKey())) {
                result += investDoubleEntry.getValue() * investDoubleEntry.getKey().getReturnRatio();

            }

        }

        return result;
    }

    /**
     * calculates the result from investments after punishments
     * @param profitFromInvestments value
     * @param player player
     * @return
     */
    private double calculateIncomeFromInvestmentsAfterPunishments(double profitFromInvestments, Player player) {
        for (String punishment : player.getPunishments()) {

            switch (punishment) {
                case "Данъчна ревизия":
                    profitFromInvestments = profitFromInvestments - (profitFromInvestments * 0.10);
                    break;

                case "Развод по котешки":
                    int number = Dice.generateTenWallDiceNumber();
                    if (number == 0 || number == 8) {
                        return 0;
                    }
                    break;
            }
        }
        return profitFromInvestments;
    }


    /**
     * @param investment
     * @return boolean that indicate if the number is positive(true) or negative(false)
     */
    private boolean calculateNumberInRiskRatio(Invest investment) {
        int number = RandomNumberGenerator.generateRandomIntegerNumber((investment.getRiskIntervalMin() * (-1) + (investment.getGetRiskIntervalMax() + 1))) + investment.getRiskIntervalMin();
        return number >= 0;
    }


    /**
     * printing the winner
     * @param player the winner
     */
    private void printWinner(Player player) {
        String message = player == null ? "Няма победител.." : String.format("Победителят е %s с %.2f останали пари", player.getName(), player.getMoney());
        System.out.println(message);
    }


}
