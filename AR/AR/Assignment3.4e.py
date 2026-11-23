#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Oct  1 00:06:43 2025

@author: kkim23
"""

import numpy as np

def projection_matrix(fx, fy, ox, oy, W, H, n, f):
    # OpenGL-style projection matrix
    return np.array([
        [2*fx/W,      0,        1 - 2*ox/W,              0],
        [0,        2*fy/H,      2*oy/H - 1,              0],
        [0,           0,   -(f+n)/(f-n),  -2*f*n/(f-n)],
        [0,           0,       -1,                       0]
    ])

def camera_to_pixel(Pc, fx, fy, ox, oy, W, H, n, f):
    P = projection_matrix(fx, fy, ox, oy, W, H, n, f)
    Pc_h = np.array([Pc[0], Pc[1], Pc[2], 1.0])
    
    #Clip space
    clip = P @ Pc_h
    
    #1/wclip
    ndc = clip[:3] / clip[3]
    x_ndc, y_ndc, z_ndc = ndc
    
    #Pixel coordinates
    u = 0.5 * (x_ndc + 1.0) * W
    v = 0.5 * (y_ndc + 1.0) * H
    
    return u, v

#Example values from Q4
W, H, n, f = 640, 480, 0.1, 10.0
fx = fy = 650
ox = oy = fx / 2.0
Pc = [0.2, -0.2, -9.0]

u, v = camera_to_pixel(Pc, fx, fy, ox, oy, W, H, n, f)
print("Pixel coords:", (u, v))