#!/bash/bin
while true; do
git pull
git add -A
git commit -a -m "Physics Parser Auto Update"
git push
sleep 300
done
