/**
 * return the width of an image
 * @param imgSrc the src of the image
 * @return the width of an image
 */
function getImgWidth(imgSrc)	{
	var newImg = new Image();
	newImg.src = imgSrc;
	var height = newImg.height;
	return newImg.width;
}
/**
 * return the height of an image
 * @param imgSrc the src of the image
 * @return the height of an image
 */
function getImgHeight(imgSrc)	{
	var newImg = new Image();
	newImg.src = imgSrc;
	return newImg.height;
}	