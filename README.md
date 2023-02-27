# Introduction

For my project I am going to design a very simplified economic sandbox "game"/simulation version of the economic model used in a game call *Victoria 3* [^1]. This is a game which seeks to simulate the socio-economic development of states during the Victorian era, hence the name, and how states can actively influence the process of society-building. The player can give it a go at the latter, by building their country and influencing or suppressing certain political groups to steer the country toward a communist utopia, Laissez-Faire libertarian society or very controversially, an ~~fascist~~ ethno-state dictatorship.
  
---  

### What will this application do?

The original game has three major systems: *economy*, *society* and *diplomacy*. But this application will primarily focus on the economy and a very simplified one at that. The user will take control of the "**state**" leading the country through industrialization by building industries, making sure they are profitable, managing supply chains and ensuring a balanced market.

To give a more detailed explanation, there are two primary systems at play: *buildings* and *the market*.
- *Buildings* convert **input** to **output** *e.g*: a tools workshop turns wood or iron into more valuable tools which can be used by other buildings. The user can influence this by using the building sector to expand buildings or change the production method to produce more goods. Users can also downsize buildings if they have to. Buildings have **income** and **expenses** determined by the prices of input, output goods and building size to determine its **profitability**.
- *The market* is where prices of goods produced by buildings are determined by **supply** and **demand** *e.g*: if supply of clothes is 100 and demand is 120 then clothes will be 20% more expensive compared to base price. The user can influence the market and therefore control the prices of goods by influencing the building system.

The goal of this application is to help users learn basic economic theories such as supply & demand and economy of scale. Another goal is to get users understand how governments or private entities identify and address needs in an economy and how certain decisions by the user may inadvertently guide the economy towards a certain direction (*e.g*: export-focused, consumer-focused or autarky economy) or even ruining it.

### Who will use this application?

The primary users of this application will be people who just started learning or are interested in economics like **highschool, university students** or even ordinary people. They can use this application to practice theories that they have learned by providing them with a sandbox environment. **Strategic gamers** and people who likes micro-managing can also enjoy this application as managing an economy can take a lot of planning ahead and precision.

### Why I choose to make this application

Although I'm planning on majoring Computer Science, I have always had an interest in history and economics. I've also seen first hand the drastic wealth inequality whenever I go on charity to the countryside of Vietnam. Therefore my lifelong goal is to help improve the livelyhood of those who are unfortunate. I believe that in order to reduce inequality, we need to change our economic system and implement effective policies. In 2020, researchers at Salesforce published a paper[^2] on *AI Economist*, an AI that can improve equality and productivity using tax policies. Reading through the paper, I realized that I can combine by interests of computer science and economics to work towards my goal. This project is a chance to combine my interests and show my passion. This is also starting point from where I can develop more sophisticated systems to model the economy.
  
---  
# User Stories

- As a user, I want to be able to add buildings to my construction queue.
- As a user, I want to be able to see a list of supply and demand or each good in the market.
- As a user, I want to be able to click on a building and see its balance.
- As a user, I want to be able to remove buildings from my construction queue.
- As a user, I want to be able to view a report of price changes in my market.


[^1]: https://store.steampowered.com/app/529340/Victoria_3/

[^2]: https://blog.salesforceairesearch.com/the-ai-economist/