var launcher = require('browser-launcher');
launcher(function (err, launch) {
    if (err) return console.error(err);

    console.log('# available browsers:');
    console.dir(launch.browsers);

    var opts = {
        headless : true,
        browser : process.argv[3],
        proxy: null
    };
    launch(process.argv[2], opts, function (err, ps) {
        if (err) return console.error(err);
        console.log('launched');
        process.exit();
    });
});