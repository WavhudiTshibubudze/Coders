const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.getDriverLocation = functions.https.onRequest((req, res) => {
    const driverId = req.query.driverId;
    admin.database().ref(`/drivers_location/${driverId}`).once('value')
        .then(snapshot => {
            if (snapshot.exists()) {
                res.status(200).json(snapshot.val());
            } else {
                res.status(404).json({ message: 'Driver not found' });
            }
        })
        .catch(error => {
            res.status(500).json({ message: error.message });
        });
});