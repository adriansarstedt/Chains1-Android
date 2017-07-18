import os

images = [ f for f in os.listdir('.')  ]

print images

for (index,filename) in enumerate(images):
  os.rename(filename,filename.lower().replace(".png", "")+"imagesmall.png")