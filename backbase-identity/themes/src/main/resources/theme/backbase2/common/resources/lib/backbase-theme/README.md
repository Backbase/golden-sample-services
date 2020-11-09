# Backbase Theme Upgrade Guide

## Why?
As theme 2 is packaged at the moment, it's not really feasible to import
this package via package.json like we normally would. Instead we need to
download the latest version from repo, and then modify it slightly to get
it to work for us. We also want to remove unnecessary files like `scss`
that aren't needed as we can only use the `css` versions. We should also
remove any assets for components that aren't strictly necessary as we
won't be using these either.

## Upgrade steps

1. Download the latest released version of the theme
This can be done in multiple ways but the simplest is to install the
theme via npm from repo and then copy it across into `lib/backbase-theme`.
It's not necessary to bring over any dependency packages like bootstrap.

2. Remove unnecessary files
Remove everything except for the `model.xml` and the `assets` and `dist`
folders. Inside the `assets/images` folder remove any sub folders but keep
the images stored separately. Inside the `dist` folder, remove everything
but the `backbase-theme.css` file.

3. Update the `backbase-theme.css` file in `dist` folder
A known bug is that the resource calls via url are incorrect.
Modify the file by finding and replacing `~@backbase/backbase-theme` with
`..`. This will tell the file to use a relative path for resources.

4. Do not remove this README file.

5. Fix any broken styling by running smoke test.
