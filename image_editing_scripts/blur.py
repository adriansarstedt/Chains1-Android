from PIL import Image
from PIL import ImageFilter

def blurImage(Name):
	img = Image.open("cover.jpg")
	blurred_image = img.filter(ImageFilter.BLUR)
	blurred_image.save(Name)

blurImage("cover.jpg")