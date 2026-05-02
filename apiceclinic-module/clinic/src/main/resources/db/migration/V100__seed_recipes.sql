-- V13: Seed 20 fit recipes for the clinic

INSERT INTO recipe (title, description, ingredients, instructions, calories, category) VALUES

('Petto di Pollo Grigliato con Erbe',
 'Fonte proteica magra ideale per chi segue un piano dietetico controllato.',
 '200g petto di pollo, rosmarino, timo, aglio, olio EVO, limone, sale, pepe',
 '1. Marinare il pollo con olio, aglio, erbe e succo di limone per 30 min. 2. Grigliare 6-7 min per lato. 3. Far riposare 5 min prima di servire.',
 220, 'PROTEICO'),

('Bowl di Quinoa con Verdure Arrosto',
 'Piatto completo ricco di proteine vegetali e fibre, ottimo per il pranzo.',
 '100g quinoa, zucchine, peperoni, cipolla rossa, ceci 80g, olio EVO, curcuma, paprika, prezzemolo',
 '1. Cuocere la quinoa. 2. Arrostire le verdure a 200°C per 20 min. 3. Unire tutto e condire con olio e spezie.',
 380, 'VEGETARIANO'),

('Salmone al Vapore con Broccoli',
 'Ricco di omega-3 e antiossidanti, perfetto per la salute cardiovascolare.',
 '180g filetto di salmone, 200g broccoli, zenzero fresco, salsa di soia light, sesamo, limone',
 '1. Cuocere il salmone al vapore 12 min. 2. Sbollentare i broccoli 5 min. 3. Condire con salsa di soia e sesamo.',
 310, 'PESCE'),

('Frittata di Albumi e Spinaci',
 'Colazione o spuntino proteico a basso contenuto di grassi.',
 '4 albumi, 1 uovo intero, 80g spinaci freschi, cipollotto, olio EVO, noce moscata, sale',
 '1. Saltare gli spinaci con cipollotto. 2. Sbattere le uova e aggiungere gli spinaci. 3. Cuocere in padella antiaderente a fuoco medio 4-5 min.',
 180, 'PROTEICO'),

('Insalata di Farro con Tonno e Pomodorini',
 'Piatto bilanciato con carboidrati complessi e proteine magre.',
 '80g farro, 120g tonno al naturale, pomodorini, cetriolo, olive nere, basilico, olio EVO, aceto di mele',
 '1. Cuocere il farro 30 min. 2. Scolare e raffreddare. 3. Mescolare con tutti gli ingredienti e condire.',
 350, 'CEREALI'),

('Zuppa di Lenticchie Rosse e Curcuma',
 'Ricca di proteine vegetali e ferro, anti-infiammatoria grazie alla curcuma.',
 '150g lenticchie rosse, carote, sedano, cipolla, curcuma, cumino, zenzero, brodo vegetale, olio EVO',
 '1. Soffriggere cipolla, carote, sedano. 2. Aggiungere lenticchie e brodo. 3. Cuocere 20 min. 4. Frullare metà zuppa per una consistenza cremosa.',
 270, 'LEGUMI'),

('Pollo Teriyaki con Riso Integrale',
 'Piatto bilanciato con proteine magre e carboidrati a basso indice glicemico.',
 '200g petto di pollo, 80g riso integrale, salsa teriyaki light, zenzero, aglio, sesamo, cipollotto',
 '1. Marinare il pollo nella salsa teriyaki 20 min. 2. Cuocere il riso integrale. 3. Cuocere il pollo in padella 5-6 min per lato. 4. Servire sul riso con sesamo.',
 420, 'PROTEICO'),

('Smoothie Bowl Proteico ai Frutti di Bosco',
 'Colazione nutriente e saziante con alto contenuto proteico.',
 '200g frutti di bosco surgelati, 1 banana, 150ml latte di mandorla, 30g proteine whey vaniglia, granola integrale 30g, semi di chia',
 '1. Frullare i frutti di bosco con banana, latte e proteine. 2. Versare in una ciotola. 3. Guarnire con granola e semi di chia.',
 340, 'COLAZIONE'),

('Merluzzo al Forno con Patate Dolci',
 'Fonte di proteine magre abbinata a carboidrati complessi e betacarotene.',
 '200g filetto di merluzzo, 200g patate dolci, rosmarino, aglio, olio EVO, paprika dolce, limone',
 '1. Tagliare le patate dolci a cubetti e arrostire a 200°C per 15 min. 2. Aggiungere il merluzzo e cuocere altri 15 min. 3. Servire con limone.',
 330, 'PESCE'),

('Insalata Greca con Tofu Marinato',
 'Versione proteica plant-based dell insalata greca classica.',
 '150g tofu compatto, cetriolo, pomodori, olive Kalamata, peperone, cipolla rossa, origano, olio EVO, limone, sale',
 '1. Marinare il tofu con olio, limone e origano per 1h. 2. Grigliare il tofu 3 min per lato. 3. Assemblare l insalata e aggiungere il tofu.',
 280, 'VEGETARIANO'),

('Avena Overnight con Mela e Cannella',
 'Colazione a rilascio lento di energia, ricca di fibre solubili.',
 '60g fiocchi d avena, 200ml latte parzialmente scremato, 1 mela, cannella, miele integrale, noci 20g',
 '1. Mescolare avena e latte in un barattolo. 2. Aggiungere mela a cubetti e cannella. 3. Refrigerare tutta la notte. 4. Aggiungere miele e noci al mattino.',
 380, 'COLAZIONE'),

('Tempeh Saltato con Verdure Asiatiche',
 'Fonte proteica fermentata ricca di probiotici, con verdure croccanti.',
 '150g tempeh, pak choi, carote, germogli di soia, salsa di soia light, olio di sesamo, zenzero, aglio, peperoncino',
 '1. Tagliare il tempeh a cubetti e rosolare in olio di sesamo. 2. Aggiungere verdure e saltare a fuoco alto 5 min. 3. Condire con salsa di soia e zenzero.',
 310, 'VEGANO'),

('Pasta Integrale al Pesto di Rucola',
 'Carboidrati complessi con grassi sani delle noci e antiossidanti della rucola.',
 '80g pasta integrale, 60g rucola, noci 30g, Parmigiano 20g, aglio, olio EVO, limone, sale',
 '1. Frullare rucola, noci, parmigiano, aglio e olio per il pesto. 2. Cuocere la pasta al dente. 3. Condire con il pesto e una spruzzata di limone.',
 420, 'CEREALI'),

('Zuppa Miso con Tofu e Alghe',
 'Minestra leggera e ricca di probiotici, ideale per supportare il microbioma intestinale.',
 '1 cucchiaio miso bianco, 100g tofu morbido, alga wakame 5g, cipollotto, brodo dashi vegetale, sesamo',
 '1. Sciogliere il miso nel brodo caldo (non bollente). 2. Aggiungere il tofu tagliato a cubetti. 3. Aggiungere le alghe reidratate. 4. Servire con cipollotto e sesamo.',
 120, 'ZUPPA'),

('Burger di Ceci e Spinaci',
 'Hamburger vegetale proteico, ottimo alternativa ai burger di carne.',
 '240g ceci cotti, 80g spinaci, cipolla, aglio, curcuma, cumino, farina integrale 30g, olio EVO, semi di sesamo',
 '1. Frullare grossolanamente ceci, spinaci e spezie. 2. Formare i burger. 3. Cuocere in padella con poco olio 4 min per lato. 4. Servire in foglia di lattuga o con pane integrale.',
 290, 'VEGANO'),

('Yogurt Greco con Frutti di Bosco e Miele',
 'Spuntino proteico e probiotico, ideale a metà mattina o come dessert.',
 '150g yogurt greco 0%, 80g frutti di bosco misti, 1 cucchiaino miele integrale, mandorle 15g, semi di lino',
 '1. Versare lo yogurt in una ciotola. 2. Aggiungere i frutti di bosco. 3. Completare con miele, mandorle e semi di lino.',
 200, 'SNACK'),

('Tagliata di Manzo con Rucola e Grana',
 'Piatto ricco di proteine e ferro, con grassi sani delle noci e antiossidanti.',
 '180g controfiletto di manzo, rucola 50g, Grana Padano 20g, olio EVO, aceto balsamico, sale grosso, pepe',
 '1. Grigliare la carne 3-4 min per lato per una cottura media. 2. Far riposare 5 min. 3. Tagliare a fette e servire su rucola con grana e aceto balsamico.',
 360, 'PROTEICO'),

('Crema di Zucca e Zenzero',
 'Zuppa vellutata ipocalorica ricca di betacarotene e proprietà antinfiammatorie.',
 '400g zucca, cipolla, zenzero fresco, brodo vegetale, latte di cocco light 50ml, olio EVO, noce moscata, semi di zucca',
 '1. Rosolare cipolla e zenzero. 2. Aggiungere zucca a cubetti e brodo. 3. Cuocere 20 min. 4. Frullare e aggiungere latte di cocco. 5. Servire con semi di zucca.',
 180, 'ZUPPA'),

('Pancakes Proteici all Avena',
 'Colazione dolce e nutriente ad alto contenuto proteico e fibre.',
 '60g fiocchi d avena macinati, 2 albumi, 1 uovo, 100g ricotta light, 1 banana, lievito in polvere, estratto di vaniglia, frutti di bosco',
 '1. Frullare tutti gli ingredienti. 2. Cuocere in padella antiaderente a fuoco medio. 3. Formare i pancakes piccoli 2-3 min per lato. 4. Servire con frutti di bosco.',
 320, 'COLAZIONE'),

('Insalata Nizzarda Fit',
 'Versione leggera della classica salade niçoise, completa e bilanciata.',
 '120g tonno al naturale, 2 uova sode, fagiolini 100g, pomodorini, cetriolo, olive nere, lattuga, acciughe 3 filetti, olio EVO, senape, limone',
 '1. Sbollentare i fagiolini 5 min. 2. Comporre l insalata con tutti gli ingredienti. 3. Emulsionare olio, limone e senape per il condimento. 4. Condire al momento di servire.',
 350, 'PESCE');
