#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 21 17:06:20 2025

@author: kkim23
"""

import numpy as np, matplotlib.pyplot as plt

fx = fy = 650.0
ox, oy = 320.0, 240.0
k1,k2,p1,p2,k3 = -0.35, 0.15, 0.025, -0.05, 0.01

def distort_and_project(Pc):
    #normalize the points
    x = Pc[0]/Pc[2]
    y = Pc[1]/Pc[2]
    r2 = x**2 + y**2
    radial = 1 + k1*r2 + k2*r2**2 + k3*r2**3
    x_star = x*radial + 2*p1*x*y + p2*(r2 + 2*x*x)
    y_star = y*radial + p1*(r2 + 2*y*y) + 2*p2*x*y
    u = fx * x_star + ox
    v = fy * y_star + oy
    return u,v

points = [np.array([-0.5,-0.5,2.0]),
          np.array([0.5,-0.5,2.0]),
          np.array([0.5,0.5,2.0]),
          np.array([-0.5,0.5,2.0])]

und = []; dist = []
for p in points:
    x, y = p[0]/p[2], p[1]/p[2]
    und.append((fx*x+ox, fy*y+oy))
    dist.append(distort_and_project(p))

print(und)
#Plotting the d vs ud
plt.figure(figsize=(5,5))
plt.scatter(*zip(*und), marker='o', label='undistorted')
plt.scatter(*zip(*dist), marker='x', label='distorted')
plt.gca().invert_yaxis()
plt.xlabel('u (px)'); plt.ylabel('v (px)')
plt.legend();
plt.grid(True)
plt.tight_layout()
plt.show()