import numpy
from math import sin, cos, sqrt, pi


class Matrices:
	def __init__(self, base_radius, neutral_side, throne_distance):
		base_radius = float(base_radius)
		neutral_side = float(neutral_side)
		throne_distance = float(throne_distance)
		a = base_radius # circumscribed circle of throne
		b = neutral_side
		c = throne_distance		
		e = base_radius * (2 * sin(36.0*pi/180.0) * sqrt(3) / 3 )
		d = base_radius * (2 * sin(36.0*pi/180.0) * ((sqrt(50+10*sqrt(5))) / (10*(sqrt(5)-1)))) + e / 2
		f = base_radius

		self.base_rotate = 72
		self.minor_rotate = 64
		self.neutral_rotate = 60

		#norm_a = numpy.matrix(a + ' 0 0; 0 ' + a + '  0; 0 0 1')
		#norm_b = numpy.matrix(b + ' 0 0; 0 ' + b + '  0; 0 0 1')
		#norm_c = numpy.matrix(c + ' 0 0; 0 ' + c + '  0; 0 0 1')
		#norm_d = numpy.matrix(d + ' 0 0; 0 ' + d + '  0; 0 0 1')

		norm_a=numpy.matrix([[ a,  0,  0],
					         [ 0, a,  0],
					         [ 0,  0,  1]])
		norm_b=numpy.matrix([[ b,  0,  0],
					         [ 0, b,  0],
					         [ 0,  0,  1]])		
		norm_c=numpy.matrix([[ c,  0,  0],
				         [ 0, c,  0],
					         [ 0,  0,  1]])
		norm_e=numpy.matrix([[ e,  0,  0],
					         [ 0, e,  0],
					         [ 0,  0,  1]])

		norm_f=numpy.matrix([[ f,  0,  0],
					         [ 0, f,  0],
					         [ 0,  0,  1]])

		self.throne = numpy.matrix([	[ 0.  ,  1  ,   1.  ], # x y z coordiantes of point 1
						        [ 0.9511,  0.3090,  1.  ],  # x y z coordiantes of point 2
						        [ 0.5878, -0.8090,  1.  ], # etc
						        [-0.5878, -0.8090,  1.  ],
						        [-0.9511,  0.3090,  1.  ]]) * norm_a

		self.base = numpy.matrix([[ 0      ,  1  , 1.  ],
					        [-0.866, -0.5 ,  1.  ],
					        [ 0.866, -0.5 ,  1.  ]]) * norm_e

		self.neutral=numpy.matrix([[ 0.  ,  0.,  1.  ],
					         [ 0.866,  0.5 ,  1.  ],
					         [ 0.866, -0.5 ,  1.  ]]) * norm_b


		self.flip_x=numpy.matrix([[-1,  0,  0],
					         [ 0,  1,  0],
					         [ 0,  0,  1]])		

		self.flip_y=numpy.matrix([[ 1,  0,  0],
					         [ 0, -1,  0],
					         [ 0,  0,  1]])

		self.to_base=numpy.matrix([[1, 0,	0],
				            [0, 1, 	0],
				            [0, c, 	1]])

		self.from_throne=numpy.matrix(	[[1, 0, 0],
					       		  	 [0, 1, 0],
					        	   	 [0, d, 1]])

		self.make_minor=numpy.matrix([[ 1  ,  0  ,  0.  ],
						       [ 0  ,  1  ,  0.  ],
						       [ 0.866*e, 0.5*e ,  1.  ]])


		self.make_neutral=numpy.matrix([	[ 1.  ,  0.  ,  0.  ],
				       				[ 0.  ,  1.  ,  0.  ],
				        			[ 0.866*b,  0.  ,  1.  ]])

	def rotate(self, a):
		a = a * pi / 180.0
		return numpy.matrix([	[ cos(a)  ,  sin(a)  ,  0.  ],
	       				[ -sin(a)  , cos(a)  ,  0.  ],
	        			[ 0.,  			0.,		1.  ]])


	def fields(self):
		for f in self.thrones():
			yield f
		for f in self.bases():
			yield f
		for f in self.neutrals():
			yield f

	def thrones(self):
		yield self.throne * self.flip_y * self.to_base
		yield self.throne * self.to_base.I # inverse
		# total: 2

	def bases(self):
		# upper throne
		for f in range(5): # adjacent to throne
			yield self.base * self.from_throne * ( self.rotate(self.base_rotate) ** f) * self.to_base 
		for f in range(5): # not adjacent to throne (minor)
			yield self.base * self.make_minor * self.rotate(self.minor_rotate) * self.to_base * self.from_throne * self.make_minor.I * self.to_base.I * ( self.rotate(self.base_rotate) ** f) * self.to_base
			yield self.base * self.make_minor * ( self.rotate(self.minor_rotate) ** 2 )* self.to_base * self.from_throne * self.make_minor.I * self.to_base.I * ( self.rotate(self.base_rotate) ** f) * self.to_base
		# total: 15

		#lower throne
		for f in range(5): # adjacent to throne
			yield self.base * self.flip_y * self.from_throne.I * ( self.rotate(self.base_rotate) ** f) * self.to_base.I 
		for f in range(5): # not adjacent to throne (minor)
			yield self.base * self.flip_y * self.make_minor.I * self.rotate(self.minor_rotate) * self.to_base.I * self.from_throne.I * self.make_minor * self.to_base * ( self.rotate(self.base_rotate) ** f) * self.to_base.I
			yield self.base * self.flip_y * self.make_minor.I * ( self.rotate(self.minor_rotate) ** 2 )* self.to_base.I * self.from_throne.I * self.make_minor * self.to_base * ( self.rotate(self.base_rotate) ** f) * self.to_base.I
		# total: 15

	def neutrals(self):
		for f in range(6):
			yield self.neutral * ( self.rotate(self.neutral_rotate) ** f) * self.make_neutral
			yield self.neutral * self.flip_x * ( self.rotate(self.neutral_rotate) ** f) * self.make_neutral.I
		# total: 12

def strip(figure):
	figure = numpy.squeeze(numpy.asarray(figure))
	result = []
	for point in figure:
		result.append([round(x,2) for x in point[:2]])
	return result

def move(figure, x, y):
	figure = figure * numpy.matrix([[1,0,0],[0,1,0],[x,y,1]])
	return figure

if __name__ == "__main__":
	import sys
	if len(sys.argv) >= 4:
		M = Matrices(*sys.argv[1:4])
		for f in M.fields():
			print str(strip(move(f, 390, 300))) + ','
