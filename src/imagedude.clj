
(ns imagedude
  (:gen-class))

(import
 '(java.awt.image PixelGrabber)
 '(javax.imageio ImageIO))

(def square-width 50)
(def square-height 50)

(defn load-image [filename]
  (ImageIO/read (ClassLoader/getSystemResource filename)))

(defn grab-pixels
  ([image]
     (let [width (.getWidth image)
	   height (.getHeight image)]
       (grab-pixels image 0 0 width height)))
  ([image x y width height]
     (let [pixels (int-array (* width height))
	   grabber (PixelGrabber. image x y width height pixels 0 width)]
       (.grabPixels grabber)
       pixels)))

(defn compare-pixel [p1 p2]
  (let [r1 (bit-and (bit-shift-right p1 16) 0xFF)
	g1 (bit-and (bit-shift-right p1 8) 0xFF)
	b1 (bit-and p1 0xFF)

	r2 (bit-and (bit-shift-right p2 16) 0xFF)
	g2 (bit-and (bit-shift-right p2 8) 0xFF)
	b2 (bit-and p2 0xFF)

	dr (- r1 r2)
	dg (- g1 g2)
	db (- b1 b2)]
    (+ (* dr dr) (* dg dg) (* db db))))

(defn compare-pixels [pixels1 pixels2]
  (reduce + (map compare-pixel pixels1 pixels2)))

