from bs4 import BeautifulSoup
import urllib
from PIL import Image

animals = []

page = urllib.urlopen("https://en.wikipedia.org/wiki/List_of_animal_names")
pageSoup = BeautifulSoup(page, "html.parser")

animalTable = pageSoup.find_all("table")[1]


for row in animalTable.find_all("tr"):
	try:
		animals.append([str(row.a.get_text()), "https://en.wikipedia.org"+str(row.a["href"])])
	except:
		pass	

animals = animals[1:len(animals)]

print animals


"""
animalNames = ['Aardvark', 'Albatross', 'Alligator', 'Alpaca', 'Ant', 'Anteater', 'Antelope', 'Ape', 'Armadillo', 'Donkey', 'Baboon', 'Badger', 'Barracuda', 'Bat', 'Bear', 'Beaver', 'Bee', 'Bird', 'Bison', 'Boar', 'Africanbuffalo', 'Butterfly', 'Camel', 'Caribou', 'Cassowary', 'Cat', 'Caterpillar', 'Cattle', 'Chamois', 'Cheetah', 'Chicken', 'Chimpanzee', 'Chinchilla', 'Chough', 'Coati', 'Cobra', 'Cockroach', 'Cod', 'Cormorant', 'Coyote', 'Crab', 'Crane', 'Crocodile', 'Crow', 'Curlew', 'Deer', 'Dinosaur', 'Dog', 'Dogfish', 'Dolphin', 'Donkey', 'Dotterel', 'Dove', 'Dragonfly', 'Duck', 'Dugong', 'Dunlin', 'Eagle', 'Echidna', 'Eel', 'Eland', 'Elephant', 'Elephantseal', 'Elk', 'Emu', 'Falcon', 'Ferret', 'Finch', 'Fish', 'Flamingo', 'Fly', 'Fox', 'Frog', 'Gaur', 'Gazelle', 'Gerbil', 'Giantpanda', 'Giraffe', 'Gnat', 'Gnu', 'Goat', 'Goldfinch', 'Goosander', 'Goose', 'Gorilla', 'Goshawk', 'Grasshopper', 'Grouse', 'Guanaco', 'Guineafowl', 'Guineapig', 'Gull', 'Hamster', 'Hare', 'Hawk', 'Hedgehog', 'Heron', 'Herring', 'Hippopotamus', 'Hornet', 'Horse', 'Hummingbird', 'Hyena', 'Ibex', 'Ibis', 'Jackal', 'Jaguar', 'Jay', 'Jellyfish', 'Kangaroo', 'Kinkajou', 'Koala', 'Komododragon', 'Kouprey', 'Kudu', 'Lapwing', 'Lark', 'Lemur', 'Leopard', 'Lion', 'Llama', 'Lobster', 'Locust', 'Loris', 'Louse', 'Lyrebird', 'Magpie', 'Mallard', 'Mammoth', 'Manatee', 'Mandrill', 'Mink', 'Mole', 'Mongoose', 'Monkey', 'Moose', 'Mouse', 'Mosquito', 'Narwhal', 'Newt', 'Nightingale', 'Octopus', 'Okapi', 'Opossum', 'Ostrich', 'Otter', 'Owl', 'Oyster', 'Panther', 'Parrot', 'Panda', 'Partridge', 'Peafowl', 'Pelican', 'Penguin', 'Pheasant', 'Pig', 'Pigeon', 'Polarbear', 'Pony', 'Porcupine', 'Porpoise', 'Prairiedog', 'Quail', 'Quelea', 'Quetzal', 'Rabbit', 'Raccoon', 'Ram', 'Rat', 'Raven', 'Reddeer', 'Redpanda', 'Reindeer', 'Rhinoceros', 'Rook', 'Salamander', 'Salmon', 'Sanddollar', 'Sandpiper', 'Sardine', 'Sealion', 'Seaurchin', 'Seahorse', 'Seal', 'Shark', 'Sheep', 'Shrew', 'Skunk', 'Sloth', 'Snail', 'Snake', 'Spider', 'Squirrel', 'Starling', 'Swan', 'Tapir', 'Tarsier', 'Termite', 'Tiger', 'Toad', 'Turkey', 'Turtle', 'Wallaby', 'Walrus', 'Wasp', 'Waterbuffalo', 'Weasel', 'Whale', 'Wolf', 'Wolverine', 'Wombat', 'Wren', 'Yak', 'Zebra']

def retrieveImage(Name, link):

	tpage = urllib.urlopen(link)
	tpageSoup = BeautifulSoup(tpage, "html.parser")

	timageLink = "https:"+str(tpageSoup.find("table", class_="infobox biota").find("a", class_="image").img["src"])
	
	timageLink = timageLink.replace("220px","800px")

	urllib.urlretrieve(timageLink, Name+"Image.jpg")

for x in range(1, len(animals)):
	try:
		retrieveImage(animalNames[x], animals[x][1])

		image = Image.open(animalNames[x]+"Image.jpg")
		image = image.point(lambda p: p * 0.7)

		image.save(animalNames[x]+"Image.jpg")
	except:
		pass
"""
