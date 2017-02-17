# __author__ = 'lllcho'
# __date__ = '2015/9/29'
import cv2
#import h5py
import os.path
from ctypes import cdll
import codecs
import numpy as np
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation, Flatten
from keras.layers.convolutional import Convolution2D, MaxPooling2D
from keras.regularizers import l2

import theano.tensor.shared_randomstreams

#from keras.utils.visualize_util import plot

letters = list('0123456789abcdefghijklmnopqrstuvwxyz')
weight_decay = 0.001
'''
nb_model = Sequential()
nb_model.add(Convolution2D(32, 1, 4, 4, border_mode='full', activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(Convolution2D(32, 32, 4, 4, activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(MaxPooling2D(poolsize=(2, 3)))
nb_model.add(Dropout(0.25))
nb_model.add(Convolution2D(64, 32, 4, 4, border_mode='full', activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(Convolution2D(64, 64, 4, 4, activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(MaxPooling2D(poolsize=(2, 3)))
nb_model.add(Dropout(0.25))
nb_model.add(Convolution2D(64, 64, 4, 4, activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(Dropout(0.25))
nb_model.add(Flatten())
nb_model.add(Dense(64 * 6 * 8, 512, activation='relu', W_regularizer=l2(weight_decay)))
nb_model.add(Dropout(0.5))
nb_model.add(Dense(512, 3, activation='softmax'))
nb_model.load_weights('model/type5_nb_model.d5')
nb_model.compile(loss='categorical_crossentropy', optimizer='adagrad')
'''
chars_model = Sequential()
chars_model.add(Convolution2D(32, 1, 4, 4, border_mode='valid', activation='relu', ))
chars_model.add(Convolution2D(32, 32, 4, 4, activation='relu', ))
chars_model.add(MaxPooling2D(poolsize=(2, 2)))
chars_model.add(Dropout(0.3))
chars_model.add(Convolution2D(64, 32, 4, 4, border_mode='full', activation='relu', ))
chars_model.add(Convolution2D(64, 64, 4, 4, activation='relu', ))
chars_model.add(MaxPooling2D(poolsize=(2, 2)))
chars_model.add(Dropout(0.3))
chars_model.add(Flatten())
chars_model.add(Dense(64 * 7 * 3, 512, activation='relu', ))
chars_model.add(Dropout(0.6))
chars_model.add(Dense(512, 36, activation='softmax'))
chars_model.load_weights("model/type5_chars_model.d5")
chars_model.compile(loss='categorical_crossentropy', optimizer='adagrad')

comp = 'type_mine'
img_dir = './image/' + comp + '/'
#f_csv = codecs.open("result/" + comp + '.csv', 'w', 'utf-8')
# for nb_img in range(1,20001):
#  name=comp+'_'+str(nb_img)+'.png'
import os

_sopen = cdll.msvcrt._sopen
_close = cdll.msvcrt._close
_SH_DENYRW = 0x10


def is_open(filename):
	if not os.access(filename, os.F_OK):
		print "not exist"
		return False
	h = _sopen(filename, 0, _SH_DENYRW, 0)
	if h == 3:
		_close(h)
		#print "not open by anyone eslse"
		return False # file is not opened by anyone else
	return True # file is already open


lastRes = ""

imgname = "hyyzm.png"

imgprevious = []

while True:
	try:
		if is_open(imgname):
			print "file is open"
			continue
		img = cv2.imread(imgname, cv2.IMREAD_GRAYSCALE)
		

		
		#print imgname
		if imgname.find("test") == -1:
			retval,t = cv2.threshold(img, 127, 1, cv2.THRESH_BINARY_INV)
		else:
			retval, t = cv2.threshold(img, 127, 1, cv2.THRESH_BINARY_INV)
		#for k in t:
		#	print k

		s = t.sum(axis=0)
		# print t
		#print s

		#print "s is:%d" %(t)
		y1, y2 = (s > np.median(s) + 5).nonzero()[0][0], (s > np.median(s) + 5).nonzero()[0][-1]
		x1, x2 = 0, 36

		#print "y1:%d, y2:%d ,x1:%d, x2:%d" %(y1, y2, x1, x2)

		if imgname.find("test") == -1:
			im = img[x1:x2, y1+20:y2-10]	
		else:
			im = img[x1:x2, y1-2:y2+3]	
		retval, im = cv2.threshold(im, 127, 255, cv2.THRESH_BINARY_INV)
		if imgname.find("test") == -1:
			retval, im = cv2.threshold(im, 127, 255, cv2.THRESH_BINARY_INV)
		#print im.shape

		im0 = im[x1:x2, 1:-1]
		if im.shape[1] < 100:
			im = np.concatenate((im, np.zeros((36, 100 - im.shape[1]), dtype='uint8')), axis=1)
		else:
			im = cv2.resize(im, (100, 36))
		I = im > 127
		I = I.astype(np.float32).reshape((1, 1, 36, 100))
		#print type(I)

		#n = nb_model.predict_classes(I, verbose=0) + 4
		n = 4
		im1 = np.zeros((36, 150), dtype=np.uint8)
		im1[:, 10:im0.shape[1] + 10] = im0
		#cv2.imwrite(imgname,im)
		#cv2.imshow('test', im)	
		#cv2.waitKey(0)

		step = im0.shape[1] / float(n)
		center = [i + step / 2 for i in np.arange(0, im0.shape[1], step).tolist()]
		#for k in center:
		#	print k  


		imgs = np.zeros((n, 1, 36, 20), dtype=np.float32)

		for i, c in enumerate(center):
			imgs[i, 0, :, :] = im1[:, c:c + 20]
		classes = chars_model.predict_classes(imgs.astype('float32') / 255.0, verbose=0)
		result = []
		for c in classes:
			result.append(letters[c])
		
		
		res = ''.join(result)
		if res == lastRes:
			continue
		
		f=open("result.txt","w+")
		
		
		f.writelines(res)
		f.close()
		lastRes = res
		
		print(''.join(result))
	except Exception,e:
		print Exception,":",e
