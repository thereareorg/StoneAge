from distutils.core import setup
import sys
sys.setrecursionlimit(3000)
import py2exe
import zmq.libzmq
setup(
	console=['test.py']
	zipfile='lib/library.zip'
	options={
		'py2exe': {
			'includes': ['zmq.backend.cython'],
			'excludes': ['zmq.libzmq'],
			'dll_excludes': ['libzmq.pyd'],
		}
	},
	data_files=[
		('lib', (zmq.libzmq.__file__,))
	]
	)