from bs4 import BeautifulSoup
import urllib
from PIL import Image
import re

def onlyascii(char):
    if ord(char) > 127: return ''
    else: return char

def createBio(name):

	page = urllib.urlopen("http://a-z-animals.com/animals/" + name.lower() + "/")
	pageSoup = BeautifulSoup(page, "html.parser")

	output = filter(onlyascii, pageSoup.find("body").findAll("table")[3].findAll("tr")[32].find("span").getText())

	headings = [x.getText() for x in pageSoup.find("body").findAll("table")[3].findAll("tr")[32].find("span").findAll("b")]

	for heading in headings:
		output = output.replace(heading, "\n"+"KKheading"+heading+"KKheading\n")

	outputFile = open(name.lower()+"information.txt", "w")
	outputFile.write(output)
	outputFile.close()

animalNames = ['Aardvark', 'Albatross', 'Alligator', 'Alpaca', 'Ant', 'Anteater', 'Antelope', 'Ape', 'Armadillo', 'Donkey', 'Baboon', 'Badger', 'Barracuda', 'Bat', 'Bear', 'Beaver', 'Bee', 'Bird', 'Bison', 'Boar', 'Africanbuffalo', 'Butterfly', 'Camel', 'Caribou', 'Cassowary', 'Cat', 'Caterpillar', 'Cattle', 'Chamois', 'Cheetah', 'Chicken', 'Chimpanzee', 'Chinchilla', 'Chough', 'Coati', 'Cobra', 'Cockroach', 'Cod', 'Cormorant', 'Coyote', 'Crab', 'Crane', 'Crocodile', 'Crow', 'Curlew', 'Deer', 'Dinosaur', 'Dog', 'Dogfish', 'Dolphin', 'Donkey', 'Dotterel', 'Dove', 'Dragonfly', 'Duck', 'Dugong', 'Dunlin', 'Eagle', 'Echidna', 'Eel', 'Eland', 'Elephant', 'Elephantseal', 'Elk', 'Emu', 'Falcon', 'Ferret', 'Finch', 'Fish', 'Flamingo', 'Fly', 'Fox', 'Frog', 'Gaur', 'Gazelle', 'Gerbil', 'Giantpanda', 'Giraffe', 'Gnat', 'Gnu', 'Goat', 'Goldfinch', 'Goosander', 'Goose', 'Gorilla', 'Goshawk', 'Grasshopper', 'Grouse', 'Guanaco', 'Guineafowl', 'Guineapig', 'Gull', 'Hamster', 'Hare', 'Hawk', 'Hedgehog', 'Heron', 'Herring', 'Hippopotamus', 'Hornet', 'Horse', 'Hummingbird', 'Hyena', 'Ibex', 'Ibis', 'Jackal', 'Jaguar', 'Jay', 'Jellyfish', 'Kangaroo', 'Kinkajou', 'Koala', 'Komododragon', 'Kouprey', 'Kudu', 'Lapwing', 'Lark', 'Lemur', 'Leopard', 'Lion', 'Llama', 'Lobster', 'Locust', 'Loris', 'Louse', 'Lyrebird', 'Magpie', 'Mallard', 'Mammoth', 'Manatee', 'Mandrill', 'Mink', 'Mole', 'Mongoose', 'Monkey', 'Moose', 'Mouse', 'Mosquito', 'Narwhal', 'Newt', 'Nightingale', 'Octopus', 'Okapi', 'Opossum', 'Ostrich', 'Otter', 'Owl', 'Oyster', 'Panther', 'Parrot', 'Panda', 'Partridge', 'Peafowl', 'Pelican', 'Penguin', 'Pheasant', 'Pig', 'Pigeon', 'Polarbear', 'Pony', 'Porcupine', 'Porpoise', 'Prairiedog', 'Quail', 'Quelea', 'Quetzal', 'Rabbit', 'Raccoon', 'Ram', 'Rat', 'Raven', 'Reddeer', 'Redpanda', 'Reindeer', 'Rhinoceros', 'Rook', 'Salamander', 'Salmon', 'Sanddollar', 'Sandpiper', 'Sardine', 'Sealion', 'Seaurchin', 'Seahorse', 'Seal', 'Shark', 'Sheep', 'Shrew', 'Skunk', 'Sloth', 'Snail', 'Snake', 'Spider', 'Squirrel', 'Starling', 'Swan', 'Tapir', 'Tarsier', 'Termite', 'Tiger', 'Toad', 'Turkey', 'Turtle', 'Wallaby', 'Walrus', 'Wasp', 'Waterbuffalo', 'Weasel', 'Whale', 'Wolf', 'Wolverine', 'Wombat', 'Wren', 'Yak', 'Zebra']

fails = []

for animal in animalNames:
	try:
		createBio(animal.lower())
	except:
		fails.append(animal)

for animal in fails:
	print animal