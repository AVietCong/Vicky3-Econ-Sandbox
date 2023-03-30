package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SandboxGUI extends JFrame {

    private static final String JSON_STORE = Paths.get(".", "data", "Sandbox.json").toString();
    private static final ImageIcon LOGO = new ImageIcon(Paths.get(".", "data", "Logo.png").toString());
    private static final String MAP = Paths.get(".", "data", "map.jpg").toString();
    private static final ImageIcon NORMAL_PRICE = new ImageIcon(Paths.get(".", "data", "NormalPrice.png").toString());
    private static final ImageIcon LOW_PRICE = new ImageIcon(Paths.get(".", "data", "LowPrice.png").toString());
    private static final ImageIcon HIGH_PRICE = new ImageIcon(Paths.get(".", "data", "HighPrice.png").toString());
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private static final int BUTTON_PANEL_HEIGHT = 26;
    private JButton saveButton;
    private JButton loadButton;
    private JButton marketButton;
    private JButton industryButton;
    private JButton constructionButton;
    private JButton nextTurnButton;
    private JLayeredPane interactionArea;
    private JPanel informationPanel;
    private String currentMenu;

    private java.util.List<Goods> allGoods;
    private java.util.List<Building> allBuildings;

    private Economy economy;
    private Industries industries;
    private Market market;
    private ConstructionSector constructionSector;

    private Goods clothes;
    private Goods fabric;
    private Goods furniture;
    private Goods grain;
    private Goods groceries;
    private Goods services;
    private Goods wood;
    private Goods coal;
    private Goods dye;
    private Goods explosives;
    private Goods fertilizer;
    private Goods glass;
    private Goods iron;
    private Goods lead;
    private Goods steel;
    private Goods sulfur;
    private Goods tools;

    private Building foodIndustry;
    private Building textileMill;
    private Building furnitureManufactory;
    private Building glassWork;
    private Building toolingWorkshop;
    private Building chemicalPlant;
    private Building steelMill;
    private Building farm;
    private Building ranch;
    private Building coalMine;
    private Building ironMine;
    private Building leadMine;
    private Building sulfurMine;
    private Building cottonPlantation;
    private Building dyePlantation;
    private Building urbanCenter;
    private Building loggingCamp;

    public SandboxGUI() throws FileNotFoundException {
        super("Economic Sandbox");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(LOGO.getImage());

        initEconomy();

        initializeGraphics();
        initializeInteraction();
        currentMenu = null;
    }



    // MODIFIES: this
    // EFFECTS: initialize the GUI with buttons in the top bar and an interaction area
    private void initializeGraphics() {
        setSize(1230, 690);
        createButtons();
        initializeInteractionArea();
        informationPanel = new JPanel();
        interactionArea.add(informationPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initialize interaction area map as a background
    private void initializeInteractionArea() {
        interactionArea = new JLayeredPane();
        add(interactionArea, BorderLayout.CENTER);
        JPanel map = new ImagePanel(MAP);
        map.setBounds(0, 0, 1580, 820);

        interactionArea.add(map, 0);
    }


    // MODIFIES: this
    // EFFECTS: add button interactions in the top bar
    private void initializeInteraction() {
        initializeSaveLoadInteraction();
        initializeReportInteraction();
    }

    // MODIFIES: this
    // EFFECTS: add interactions to buttons related to data persistence in the top bar
    private void initializeSaveLoadInteraction() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSandbox();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSandbox();
            }
        });

        nextTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTurn();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: add interactions to buttons related to the simulation in the top bar
    private void initializeReportInteraction() {
        marketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMarketReport();
            }
        });

        industryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIndustryReport();
            }
        });

        constructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleConstructionReport();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: create an information panel of construction queue and display it inside the interaction area
    private void handleConstructionReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setBounds(0,0, 300, 785);
        GridBagConstraints gbc = giveInfoPanelConstraints();

        addConstructionHeader(gbc);
        gbc.weighty = 0.1;
        gbc.weightx = 0.1;
        gbc.gridy += 1;
        gbc.gridx = 0;
        addConstructionReport(gbc);
        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "c";
    }

    // MODIFIES: this
    // EFFECTS: add rows for each building in construction queue and a progress bar to show how much is of it is built
    private void addConstructionReport(GridBagConstraints gbc) {
        for (int i = 0; i < economy.getConstructionSector().getConstructionQueue().size(); i++) {
            gbc.fill = GridBagConstraints.VERTICAL;
            String name = economy.getConstructionSector().getConstructionQueue().get(i).getName();
            informationPanel.add(new JLabel(name), gbc);
            gbc.gridx += 1;
            gbc.weightx = 0.5;
            JProgressBar progressBar = new JProgressBar();
            progressBar.setStringPainted(true);
            double remaining = (double) economy.getConstructionSector().getConstructionValue().get(i);
            int cost = economy.getConstructionSector().getConstructionQueue().get(i).getConstructionCost();
            int progress = (int) (100 * (1 - (remaining / cost)));
            progressBar.setValue(progress);
            informationPanel.add(progressBar, gbc);
            gbc.gridx = 0;
            gbc.gridy += 1;
            gbc.weightx = 0.1;
        }
    }

    // MODIFIES: this
    // EFFECTS: add headers in the construction menu
    private void addConstructionHeader(GridBagConstraints gbc) {
        gbc.ipadx = 15;

        informationPanel.add(new JLabel("Building"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Remaining"), gbc);
    }

    // MODIFIES: this
    // EFFECTS: create an information panel summarizing all industries and display it inside the interaction area
    private void handleIndustryReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setBounds(0,0, 500, 785);
        GridBagConstraints gbc = giveInfoPanelConstraints();

        addIndustryReportHeader(gbc);
        gbc.weighty = 0.1;
        for (Building building : economy.getIndustries().removeEmptyBuildings().getAllBuildings()) {
            gbc.gridy += 1;
            gbc.gridx = 0;
            addRowsForActiveBuilding(building, gbc);
        }
        for (Building building : economy.getIndustries().returnEmptyBuildings().getAllBuildings()) {
            gbc.gridy += 1;
            gbc.gridx = 0;
            addRowsForInactiveBuilding(building, gbc);
        }
        gbc.gridy += 1;
        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "i";
    }

    // MODIFIES: this
    // EFFECTS: add information about all buildings that are of size == 0
    private void addRowsForInactiveBuilding(Building building,GridBagConstraints gbc) {
        gbc.ipadx = 10;
        informationPanel.add(new JLabel(building.getName()), gbc);
        gbc.gridx += 1;
        gbc.ipadx = 5;
        informationPanel.add(new JLabel(), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel(), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel(), gbc);
        gbc.gridx += 1;
        addBuildingSizeLabel(building, gbc);
        gbc.gridx += 1;
        addBuildButton(building, gbc);
        gbc.gridx += 1;
        if (economy.getConstructionSector().numInQueue(building) != 0) {
            addRemoveButton(building, gbc);
        }
        informationPanel.add(new JLabel(), gbc);
    }

    // MODIFIES: this
    // EFFECTS: add information about all buildings that are of size != 0
    private void addRowsForActiveBuilding(Building building, GridBagConstraints gbc) {
        gbc.ipadx = 10;
        informationPanel.add(new JLabel(building.getName()), gbc);
        gbc.gridx += 1;
        gbc.ipadx = 5;
        informationPanel.add(new JLabel("£" + building.getIncome()), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("£" + building.getExpense()), gbc);
        gbc.gridx += 1;
        addProfitLabel(building, gbc);
        gbc.gridx += 1;
        addBuildingSizeLabel(building, gbc);
        gbc.gridx += 1;
        addBuildButton(building, gbc);
        gbc.gridx += 1;
        addRemoveButton(building, gbc);
    }

    // MODIFIES: this
    // EFFECTS: display profits of a building in industry menu and color coded green for profitable, red for not
    private void addProfitLabel(Building building, GridBagConstraints gbc) {
        JLabel profitLabel = new JLabel("£" + building.returnProfits());
        if (building.isProfitable()) {
            profitLabel.setForeground(Color.GREEN);
        } else {
            profitLabel.setForeground(Color.red);
        }
        informationPanel.add(profitLabel, gbc);
    }

    // MODIFIES: this
    // EFFECTS: add information about a building in the industry menu
    private void addBuildingSizeLabel(Building building, GridBagConstraints gbc) {
        JLabel sizeLabel;
        if (economy.getConstructionSector().numInQueue(building) != 0) {
            String sizeString = building.getSize() + "+" + economy.getConstructionSector().numInQueue(building);
            sizeLabel = new JLabel(sizeString);
        } else {
            sizeLabel = new JLabel(String.valueOf(building.getSize()));
        }
        informationPanel.add(sizeLabel, gbc);
    }

    // MODIFIES: this
    // EFFECTS: add button to downsize buildings or remove buildings from construction queue
    private void addRemoveButton(Building building, GridBagConstraints gbc) {
        JButton removeButton = new JButton("-");
        if (economy.getConstructionSector().numInQueue(building) > 0) {
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    economy.getConstructionSector().remove(building);
                    handleIndustryReport();
                }
            });
        } else {
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    building.downsize(1);
                    handleIndustryReport();
                }
            });
        }
        informationPanel.add(removeButton, gbc);
    }

    // MODIFIES: this
    // EFFECTS: add button to build buildings inside industry menu
    private void addBuildButton(Building building, GridBagConstraints gbc) {
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                economy.getConstructionSector().build(building);
                handleIndustryReport();
            }
        });
        informationPanel.add(addButton, gbc);
    }

    // MODIFIES: this
    // EFFECTS: add headers for the industry menu
    private void addIndustryReportHeader(GridBagConstraints gbc) {
        gbc.ipadx = 10;
        informationPanel.add(new JLabel("Factory"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Income"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Expense"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Profit"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Size"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Build"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Remove"), gbc);
    }

    // MODIFIES: this
    // EFFECTS: create an information panel summarizing market info and display it inside the interaction area
    private void handleMarketReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setBounds(0,0, 300, 785);
        GridBagConstraints gbc = giveInfoPanelConstraints();

        List<Goods> activeGoods = economy.getMarket().removeInactiveGoods().getAllGoods();
        addMarketReportHeader(gbc);
        gbc.weighty = 0.1;
        gbc.gridy += 1;
        gbc.gridx = 0;
        for (Goods goods : activeGoods) {
            addRowForGood(goods, gbc);
            gbc.gridy += 1;
            gbc.gridx = 0;
        }
        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "m";
    }

    // MODIFIES: this
    // EFFECTS: add the header for the Market menu
    private void addMarketReportHeader(GridBagConstraints gbc) {
        gbc.ipadx = 5;

        informationPanel.add(new JLabel("Goods"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Demand"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Supply"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Price"), gbc);
    }

    // MODIFIES: this
    // EFFECTS: add information about a good in the information panel
    private void addRowForGood(Goods goods, GridBagConstraints gbc) {
        JLabel nameLabel = new JLabel(goods.getName());
        JLabel demandLabel = new JLabel(String.valueOf(goods.getDemand()));
        JLabel supplyLabel = new JLabel(String.valueOf(goods.getSupply()));
        JLabel priceLabel = new JLabel("£" + goods.determinePrice());
        setPriceIcon(goods, priceLabel);

        gbc.ipadx = 5;
        informationPanel.add(nameLabel, gbc);
        gbc.gridx += 1;
        informationPanel.add(demandLabel, gbc);
        gbc.gridx += 1;
        informationPanel.add(supplyLabel, gbc);
        gbc.gridx += 1;
        informationPanel.add(priceLabel, gbc);
    }

    // EFFECTS: assign a price icon to a label to signal how expensive a good is
    private void setPriceIcon(Goods goods, JLabel priceLabel) {
        if (goods.determinePriceModifier() >= 1.25) {
            priceLabel.setIcon(HIGH_PRICE);
        } else if (goods.determinePriceModifier() <= 0.75) {
            priceLabel.setIcon(LOW_PRICE);
        } else {
            priceLabel.setIcon(NORMAL_PRICE);
        }
        priceLabel.setHorizontalTextPosition(JLabel.LEFT);
    }

    // MODIFIES: this
    // EFFECTS: add buttons to the top bar
    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(100, BUTTON_PANEL_HEIGHT));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        add(buttonPanel, BorderLayout.NORTH);

        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        marketButton = new JButton("Market");
        industryButton = new JButton("Industry");
        constructionButton = new JButton("Construction");
        nextTurnButton = new JButton("Next Turn");

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(marketButton);
        buttonPanel.add(industryButton);
        buttonPanel.add(constructionButton);
        buttonPanel.add(nextTurnButton);
    }

    // MODIFIES: this
    // EFFECTS: refresh the interaction area with the updated information in the information panel
    private void refreshInteractionArea() {
        interactionArea.add(informationPanel, Integer.valueOf(1));
        revalidate();
        repaint();
    }

    // EFFECTS: return GridBagConstraints for headers inside information panel
    private GridBagConstraints giveInfoPanelConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 0.2;
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
    }

    // EFFECTS: save sandbox to file
    private void saveSandbox() {
        try {
            jsonWriter.open();
            jsonWriter.write(economy);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads economy from file
    private void loadSandbox() {
        try {
            economy = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: add all goods to the market
    private void setupMarket() {
        market = new Market();
        for (Goods goods : allGoods) {
            market.addGoods(goods);
        }
    }

    // MODIFIES: this
    // EFFECTS: process the economy in preparation for next turn
    private void processTurn() {
        constructAndDestroyBuildings();
        exchangeGoods();
        cleanupMarket();
        consumeAndProduce();
        if (currentMenu == "c") {
            handleConstructionReport();
        } else if (currentMenu == "m") {
            handleMarketReport();
        } else {
            handleIndustryReport();
        }
    }

    // MODIFIES: this
    // EFFECTS: change the demand and supply of goods in market
    private void consumeAndProduce() {
        for (Building b : economy.getIndustries().getAllBuildings()) {
            b.consume();
            b.produce();
        }
    }

    // MODIFIES: this
    // EFFECTS: reset demand and supply for all but consumer goods; consumer goods gain 5 demand
    private void cleanupMarket() {
        for (Goods goods : economy.getMarket().returnIndustrialGoods()) {
            goods.setDemand(0);
            goods.setSupply(0);
        }
        for (Goods goods : economy.getMarket().returnConsumerGoods()) {
            goods.setSupply(0);
            goods.addDemand(5);
        }
    }

    // MODIFIES: this
    // EFFECTS: set income and expenses of buildings
    private void exchangeGoods() {
        for (Building b : economy.getIndustries().getAllBuildings()) {
            b.payExpense();
            b.gainIncome();
        }
    }

    // MODIFIES: this
    // EFFECTS: construct new buildings remove buildings with size 0 from industry
    private void constructAndDestroyBuildings() {
        if (!economy.getConstructionSector().getConstructionQueue().isEmpty()) {
            economy.getConstructionSector().consume();
            economy.getConstructionSector().payExpense();
        }
        List<Building> constructedBuilding = economy.getConstructionSector().construct();
        for (Building b : constructedBuilding) {
            if (economy.getIndustries().getAllBuildings().contains(b)) {
                b.expand();
            } else {
                economy.getIndustries().add(b);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: create starting economic conditions
    private void initEconomy() {
        initGoods();
        allGoods = Arrays.asList(clothes, fabric, furniture, grain, groceries, services, wood, coal, dye,
                explosives, fertilizer, glass, iron, lead, steel, sulfur, tools);
        initIndustryBuildings();
        initAgricultureBuildings();
        initOtherBuildings();
        allBuildings = Arrays.asList(foodIndustry, textileMill, furnitureManufactory, glassWork, toolingWorkshop,
                chemicalPlant, steelMill, urbanCenter, farm, ranch, cottonPlantation, dyePlantation, coalMine, ironMine,
                leadMine, sulfurMine);
        constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives), Arrays.asList(70, 20, 20));
        setupIndustry();
        setupMarket();
        setDemandForConsumerGoods();
        for (Building b : industries.getAllBuildings()) {
            b.consume();
            b.produce();
            b.payExpense();
            b.gainIncome();
        }
        economy = new Economy(industries, market, constructionSector);
    }

    // MODIFIES: this
    // EFFECTS: set demand of consumer goods
    private void setDemandForConsumerGoods() {
        clothes.setDemand(410);
        furniture.setDemand(289);
        grain.setDemand(313);
        services.setDemand(425);
    }

    // MODIFIES: this
    // EFFECTS: initializes all goods available for the sandbox
    private void initGoods() {
        clothes = new Goods("Clothes", 30, Goods.GoodsType.CONSUMER);
        fabric = new Goods("Fabric", 20, Goods.GoodsType.INDUSTRIAL);
        furniture = new Goods("Furniture", 30, Goods.GoodsType.CONSUMER);
        grain = new Goods("Grain", 20, Goods.GoodsType.INDUSTRIAL);
        groceries = new Goods("Groceries", 30, Goods.GoodsType.CONSUMER);
        services = new Goods("Services", 30, Goods.GoodsType.CONSUMER);
        wood = new Goods("Wood", 20, Goods.GoodsType.INDUSTRIAL);
        coal = new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL);
        dye = new Goods("Dye", 40, Goods.GoodsType.INDUSTRIAL);
        explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
        fertilizer = new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL);
        glass = new Goods("Glass", 40, Goods.GoodsType.INDUSTRIAL);
        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        lead = new Goods("Lead", 40, Goods.GoodsType.INDUSTRIAL);
        steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
        sulfur = new Goods("Sulfur", 50, Goods.GoodsType.INDUSTRIAL);
        tools = new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL);
    }

    // MODIFIES: this
    // EFFECTS: initializes all agricultural buildings
    private void initAgricultureBuildings() {
        farm = new Building("Farm", 150,
                Arrays.asList(fertilizer), Arrays.asList(40),
                Arrays.asList(grain), Arrays.asList(150));
        ranch = new Building("Livestock Ranch", 150,
                Arrays.asList(grain), Arrays.asList(10),
                Arrays.asList(fabric, fertilizer), Arrays.asList(40, 10));
        cottonPlantation = new Building("Cotton Plantation", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(fabric), Arrays.asList(40));
        dyePlantation = new Building("Dye Plantation", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(dye), Arrays.asList(25));
    }

    // MODIFIES: this
    // EFFECTS: initializes all industrial buildings
    private void initIndustryBuildings() {
        foodIndustry = new Building("Food Industry", 450,
                Arrays.asList(grain, iron), Arrays.asList(50, 20),
                Arrays.asList(groceries), Arrays.asList(65));
        textileMill = new Building("Textile Mill", 450,
                Arrays.asList(fabric, dye, tools), Arrays.asList(60, 10, 5),
                Arrays.asList(clothes), Arrays.asList(100));
        furnitureManufactory = new Building("Furniture Manufactory", 450,
                Arrays.asList(wood, fabric, tools), Arrays.asList(50, 10, 10),
                Arrays.asList(furniture), Arrays.asList(110));
        glassWork = new Building("Glass Work", 450,
                Arrays.asList(lead), Arrays.asList(35),
                Arrays.asList(glass), Arrays.asList(70));
        toolingWorkshop = new Building("Tooling Workshop", 450,
                Arrays.asList(wood, steel), Arrays.asList(30, 20),
                Arrays.asList(tools), Arrays.asList(80));
        chemicalPlant = new Building("Chemical Plant", 450,
                Arrays.asList(sulfur, iron, coal), Arrays.asList(60, 30, 15),
                Arrays.asList(fertilizer, explosives), Arrays.asList(110, 70));
        steelMill = new Building("Steel Mill", 450,
                Arrays.asList(iron, coal), Arrays.asList(90, 30),
                Arrays.asList(steel), Arrays.asList(120));
    }

    // MODIFIES: this
    // EFFECTS: initializes remaining buildings
    private void initOtherBuildings() {
        coalMine = new Building("Coal Mine", 300,
                Arrays.asList(tools, explosives), Arrays.asList(15, 10),
                Arrays.asList(coal), Arrays.asList(85));
        ironMine = new Building("Iron Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(iron), Arrays.asList(80));
        leadMine = new Building("Lead Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(coal), Arrays.asList(80));
        sulfurMine = new Building("Sulfur Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(coal), Arrays.asList(80));
        loggingCamp = new Building("Logging Camp", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(wood), Arrays.asList(60));
        urbanCenter = new Building("Urban Center", 450,
                Arrays.asList(glass, steel, coal), Arrays.asList(10, 10, 10),
                Arrays.asList(services), Arrays.asList(110));
    }

    // MODIFIES: this
    // EFFECTS: set up the starting industries
    private void setupIndustry() {
        List<Building> startingIndustries = Arrays.asList(textileMill, toolingWorkshop, urbanCenter, farm,
                ranch, ironMine, loggingCamp);
        industries = new Industries();
        for (Building b : allBuildings) {
            industries.add(b);
            if (!startingIndustries.contains(b)) {
                b.downsize(1);
            }
        }
        textileMill.setSize(3);
        urbanCenter.setSize(8);
        farm.setSize(11);
        ranch.setSize(5);
        ironMine.setSize(7);
        loggingCamp.setSize(16);
    }
}
