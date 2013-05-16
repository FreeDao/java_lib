module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('../package.json'),
    cancompile : {
      dist : {
        // Compile all ejs files
        src : [
          'views/pagecontent-views/*.ejs',
          'views/pagecontent-views/aqliviews/*.ejs',
          'views/header-views/*.ejs',
          'views/init.ejs'
        ],
        // Save to views.production.js
        out : '../views.facebook.production.js'
      }
    },
    uglify: {
      dist: {
        src:
            ['../views.facebook.production.js',
             'js/controller.js',
             'js/model.js',
             'js/util.js',
             'facebook.prod.js'
            ],
        dest: 'release/<%= pkg.version %>/facebook/facebook/facebook.js'
      }
    },
    cssmin: {
       release: {
         src: ['facebook.css'],
         dest: 'release/<%= pkg.version %>/facebook/facebook/facebook.css'
       },
     },
    copy: {
      appImages: {
        files: [
          {src : ['images/shared/*'], dest : 'release/<%= pkg.version %>/facebook/facebook/'},
          {src : ['images/480x272/*'], dest : 'release/<%= pkg.version %>/facebook/facebook/'},
          {src : ['images/800x480/*'], dest : 'release/<%= pkg.version %>/facebook/facebook/'}
        ]
      },
      locales: {
        files: {
          'release/<%= pkg.version %>/facebook/facebook/': 'locales/*'
        }
      }
    },
    compress: {
      target: {
        options: {
           archive: '../release/facebook.zip',
           mode : 'zip'
        },
        files: [{
           expand: true,
           cwd : 'release/<%= pkg.version %>/facebook/facebook/',
           src : ['**'],
           dest : 'facebook/'
        }]
      }
    },

    clean: {
      target: ['release'],
      temp : {
              options: {
                force : true
              },

              src : ['../views.facebook.production.js']

              }

    },
    /*
    removelogging: {
      dist: {
        src : "release/facebook/<%= pkg.version %>/facebook.js",
        dest: "release/facebook/<%= pkg.version %>/facebook.js",
        options: {}
      }
    }
    replace: {
      pathFix: {
        src: [
          'release/facebook/<%= pkg.version %>/release.min.js',
          'release/facebook/<%= pkg.version %>/release-min.css'
        ],
        overwrite: true,
        replacements: [{
          from: '../images',
          to: '../../gimages'
        }]
      }
    }*/
  });


grunt.loadNpmTasks('grunt-contrib');
grunt.loadNpmTasks('grunt-css');
grunt.loadNpmTasks('can-compile');
grunt.loadNpmTasks('grunt-contrib-clean');
grunt.loadNpmTasks('grunt-remove-logging');

//Default task.
//grunt.registerTask('default', 'clean cancompile min cssmin copy:appImages copy:locales compress');
grunt.registerTask('default', ['clean', 
                               'cancompile', 
                               'uglify',
                               'cssmin', 
                               'copy:appImages',
                               'copy:locales',
                               'compress',
                               'clean:temp']);
//grunt.registerTask('default', 'clean cancompile min removelogging cssmin copy:appImages copy:locales compress copy:zip');
//grunt.registerTask('default', 'clean cancompile min cssmin replace copy:font copy:appImages compress copy:zip');

};
