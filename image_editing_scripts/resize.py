import os
from PIL import Image

images = [ f for f in os.listdir('.')]

for (index,filename) in enumerate(images):
	try:
		image = Image.open(filename)
		image = image.resize((100, 100), Image.ANTIALIAS)
		image.save(filename)
	except:
		pass