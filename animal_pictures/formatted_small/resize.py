import os
from PIL import Image

images = [ f for f in os.listdir('.')]

for (index,filename) in enumerate(images):
	try:
		image = Image.open(filename)
		image = image.resize((186, 120), Image.ANTIALIAS)
		image.save(filename)
	except:
		pass