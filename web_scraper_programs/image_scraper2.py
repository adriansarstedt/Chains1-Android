from bs4 import BeautifulSoup
import urllib
from PIL import Image

animal = "Wren"

def retrieveImage(Name, link):

	tpage = urllib.urlopen(link)
	tpageSoup = BeautifulSoup(tpage, "html.parser")

	timageLink = "https:"+str(tpageSoup.find("table", class_="infobox biota").find("a", class_="image").img["src"])

	timageLink = timageLink.replace("220px","800px")

	urllib.urlretrieve(timageLink, Name+"Image.jpg")

retrieveImage(animal, "https://en.wikipedia.org/wiki/Wren")

image = Image.open(animal+"Image.jpg")
image = image.point(lambda p: p * 0.8)

image.save(animal+"Image.jpg")