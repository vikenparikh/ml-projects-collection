#!/bin/sh
rm -rf *.h264
wget --content-disposition http://206.207.50.7/getvideo
video_file_name=$(ls *.h264)
Xvfb :1 & export DISPLAY=:1
./darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg yolov3-tiny.weights ./$video_file_name -dont_show -ext_output > result
./darknet_test.py
echo $video_file_name
output=$(cat result_label)
echo $output
