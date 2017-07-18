import os
from PIL import Image

images = [ f for f in os.listdir('.')]

for (index,filename) in enumerate(images):
	try:
		image = Image.open(filename)
		image = image.point(lambda p: p*0.7)
		image.save(filename)
	except:
		pass
	