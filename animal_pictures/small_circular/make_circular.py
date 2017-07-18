import os
from PIL import Image, ImageOps

images = [ f for f in os.listdir('.')]
mask = Image.open('alpha_mask.png').convert('L')

for (index,filename) in enumerate(images):
	try:
		im = Image.open(filename)
		output = ImageOps.fit(im, mask.size, centering=(0.5, 0.5))
		output.putalpha(mask)
		output.save(filename)
	except:
		pass


