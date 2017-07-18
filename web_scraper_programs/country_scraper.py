from bs4 import BeautifulSoup
import urllib
from PIL import Image
import re

animals = []

page = urllib.urlopen("https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population")
pageSoup = BeautifulSoup(page, "html.parser")

countries = []

countryTable = pageSoup.find_all("table")[1]

for row in countryTable.find_all("tr"):
	try:
		name = str(row.find_all("td")[1].get_text())
		name = re.sub(r'\([^)]*\)', '', name)
		name = re.sub(r'\[[^)]*\]', '', name)
		countries.append(name)
	except:
		pass	

for x in range(len(countries)):
	if (countries[x][0] == " "):
		countries[x] = countries[x][1:]

print countries
