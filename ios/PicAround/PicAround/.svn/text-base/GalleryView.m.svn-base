//
//  GalleryView.m
//  PicAround
//
//  Created by BenA on 2/10/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "GalleryView.h"
#import "PACollectionViewCell.h"
#import "PAImageDetailController.h"
#import "MWPhoto.h"
#import <QuartzCore/QuartzCore.h>

@implementation GalleryView

-(void) viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"Gallery Thumbs Screen";
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendView:@"Gallery Thumbs Screen"];
    //[self loadSelf];
    _currentSelectedEventID = @"";
    userDidUploadFromGallery = NO;
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(becomeActive:)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTintColor:[UIColor colorWithRed:45.0/255.0 green:150.0/255.0 blue:230.0/255.0 alpha:0]];
    //self.collectionView.backgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"Icons/iphone4wallpapers-simpletexture.png"]];
    
    [self.navigationController.navigationBar setAlpha:0.8];
    flashMode =  UIImagePickerControllerCameraFlashModeAuto;
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc]init];
    self.refreshControl = refreshControl;
    [refreshControl addTarget:self action:@selector(loadSelfPull)forControlEvents:UIControlEventValueChanged];
    [self.tableView addSubview:refreshControl];
    self.tableView.alwaysBounceVertical = YES;
    
}
-(void)loadSelfPull
{
    _currentSelectedEventID = @"";
    [self loadSelf];
}
-(void)becomeActive:(NSNotification *)notification {
    
    // only respond if the selected tab is our current tab
    if (self.tabBarController.selectedIndex == 2) { // Tab 1 is 0 index, Tab 2 is 1, etc
        //[self viewDidAppear:YES];
        [self loadSelf];
    }
    
}
-(void) viewDidAppear:(BOOL)animated
{
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    if (!appDelegate.DetailedViewSelected)
    {
        [self loadSelf];
    }
    [self.toolbarSpinner stopAnimating];
    appDelegate.DetailedViewSelected =  NO;
    //self.navigationController.navigationItem.backBarButtonItem.title = @"Events" ;//= [[UIBarButtonItem alloc] initWithTitle:@"Events" style:UIBarButtonItemStyleBordered target:nil action:nil];
}

-(void) loadSelf
{
    [self.toolbarSpinner startAnimating];
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    if (appDelegate.EventID != [NSNull null])
    {
        if(![_currentSelectedEventID isEqualToString:appDelegate.EventID])
        {
            [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
            
            _currentSelectedEventID = appDelegate.EventID;
            _galleryNavigation.title = appDelegate.EventName;
            self.title = appDelegate.EventName;
            self.searches = [@[] mutableCopy];
            self.searchResults = [@[] mutableCopy];
            self.paApi = [[PicAroundApi alloc]init];
            picturesDic = [[NSMutableDictionary alloc] init];
            UploadStatus = [[NSMutableDictionary alloc] init];
            [self.collectionView reloadData];
            
            PAPhotoEntity *newPhoto = [[PAPhotoEntity alloc] init];
            UIImage *image = [UIImage imageNamed:@"iphoneCamera.png"];
            newPhoto.Thumbnail =image;
            newPhoto.LargeImage =image;
            [self.searchResults addObject:newPhoto];
            
            //[self.collectionView insertItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:[self.searchResults count]-1 inSection:0]]];
            
            [self.collectionView clearsContextBeforeDrawing];
            [self.collectionView reloadData];
            
            
            [self.paApi albumImagesById: appDelegate.EventID completionBlock:^(NSString *searchTerm, NSArray *results, NSError *error) {
                
                //[self.spinner stopAnimating];
                //self.spinner.hidesWhenStopped = YES;
                
                [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                if(results && [results count] > 0)
                {
                    [self.searchResults addObjectsFromArray: results];
                    dispatch_async(dispatch_get_main_queue(), ^{
                        //[self.spinner stopAnimating];
                        //self.spinner.hidesWhenStopped = YES;
                        [self.toolbarSpinner stopAnimating];
                        self.toolbarSpinner.hidesWhenStopped = YES;
                        [self.refreshControl endRefreshing];
                        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                        [self.collectionView reloadData];
                        
                    });
                }
            } imageLoadedEvent:^(NSString *imageIndex, UIImage *photoImage) {
                PACollectionViewCell *cell = [picturesDic objectForKey:imageIndex];
                if(cell!=nil)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        //NSLog(@"Setting image at index %@", imageIndex);
                        cell.imageView.image = nil;
                        [cell.imageView setImage:photoImage];
                        [cell.spinner stopAnimating];
                        //[self.collectionView reloadData];
                    });
                }
            }];
        }
    }
}

#pragma mark - UICollectionView Datasource
// 1
- (NSInteger)collectionView:(UICollectionView *)view numberOfItemsInSection:(NSInteger)section {
    
    return [self.searchResults count];
}
// 2
- (NSInteger)numberOfSectionsInCollectionView: (UICollectionView *)collectionView {
    return 1;
}
// 3
- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    //NSLog(@"index Path %i", indexPath.row);
    PACollectionViewCell *cell =  (PACollectionViewCell *)[cv dequeueReusableCellWithReuseIdentifier:@"Cell" forIndexPath:indexPath];
    NSString* indexPathRow = [NSString stringWithFormat:@"%i",indexPath.row];
    PACollectionViewCell *cellAtindexPathRow = [picturesDic objectForKey:indexPathRow];
    if (cellAtindexPathRow == nil)
    {
        PAPhotoEntity * enity = [self.searchResults objectAtIndex:[indexPath row]];
        UIImage *localThumbnail = enity.Thumbnail;
        if(localThumbnail != nil)
        {
            //NSLog(@"cellAtindexPathRow index Path %i is nil BUT IMAGE OK setting image",indexPath.row);
            cell.spinner.hidesWhenStopped = YES;
            [cell.spinner stopAnimating];
            cell.imageView.image  = localThumbnail;
            cell.userName.text = enity.UserId;
        }
        else
        {
            //NSLog(@"cellAtindexPathRow index Path %i is nil AND NO IMAGE YET",indexPath.row);
            cell.spinner.hidesWhenStopped = YES;
            [cell.spinner startAnimating];
            cell.imageView.image  = nil;
        }
        [picturesDic setObject:cell forKey:indexPathRow];
    }
    else
    {
        PAPhotoEntity * enity = [self.searchResults objectAtIndex:[indexPath row]];
        UIImage *localThumbnail = enity.Thumbnail;
        if(localThumbnail != nil)
        {
            //NSLog(@"Cached object at index %i", indexPath.row);
            
            cell.spinner.hidesWhenStopped = YES;
            [cell.spinner stopAnimating];
            cell.imageView.image  = localThumbnail;
            cell.userName.text = enity.UserId;
        }
        else
        {
            //NSLog(@"Cached object at index %i", indexPath.row);
            cell.spinner.hidesWhenStopped = YES;
            [cell.spinner stopAnimating];
        }
        
    }
    
    NSString* status = [UploadStatus objectForKey: indexPathRow];
    if (status == nil)
    {
        cell.uploadImageProgress.hidden = YES;
        cell.ok.hidden = YES;
    }else if (status == @"Uploading")
    {
        cell.uploadImageProgress.hidden = NO;
        cell.ok.hidden = YES;
    }else if(status == @"Done")
    {
        cell.uploadImageProgress.hidden = YES;
        cell.ok.hidden = NO;
    }
        cell.contentView.clipsToBounds = YES;
        cell.contentView.layer.cornerRadius = 5.0;
        cell.contentView.layer.masksToBounds = YES;
    //    cell.imageView.layer.borderColor = [UIColor grayColor].CGColor;
    //    cell.imageView.layer.borderWidth = 1.0;
    cell.contentView.layer.borderColor = [[UIColor whiteColor] CGColor];
    cell.contentView.layer.borderWidth = 3;
    return cell;
    
    //cell.imageView.image = load image from internet;
    
    
    //    UIImage *localThumbnail = ((PAPhotoEntity *)[self.searchResults objectAtIndex:[indexPath row]]).Thumbnail;
    //    if(localThumbnail!=nil)
    //    {
    //        cell.imageView.image  = localThumbnail;
    //        [cell.spinner stopAnimating];
    //    }
    //  return cell;
}
// 4
/*- (UICollectionReusableView *)collectionView:
 (UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
 {
 return [[UICollectionReusableView alloc] init];
 }*/

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (NSUInteger)numberOfPhotosInPhotoBrowser:(MWPhotoBrowser *)photoBrowser {
    return self.photos.count;
}

- (NSString*)GetEventId {
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];

    return appDelegate.EventID;
}

- (NSString*)GetEventName {
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    return appDelegate.EventName;
}

- (MWPhoto *)photoBrowser:(MWPhotoBrowser *)photoBrowser photoAtIndex:(NSUInteger)index {
    if (index < self.photos.count)
        return [self.photos objectAtIndex:index];
    return nil;
}

#pragma mark - UICollectionViewDelegate
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    selectedIndex = indexPath;
    selectedIndexRow = indexPath.row;
    if(indexPath.row != 0)
    {
        
        //[self performSegueWithIdentifier:@"PAPhotoDetailSegue" sender:self];
        // Create array of `MWPhoto` objects
        self.photos = [NSMutableArray array];
        
        int i=0;
        
        for(PAPhotoEntity *objPhoto in self.searchResults)
        {
            if (i!=0)
            {
                if (objPhoto.LargeImage == nil)
                {
                    NSString *linkToMidQImage = [PicAroundApi picAroundFullLinkURLForimageLink:objPhoto.MidQLink];
                    MWPhoto *photo = [MWPhoto photoWithURL:[NSURL URLWithString:linkToMidQImage]];
                    
//                    NSString * userID = [NSString stringWithFormat:@"%@",objPhoto.UserId];
//                    NSDictionary* params = [NSDictionary dictionaryWithObject:@"id,name" forKey:@"fields"];
//                    FBRequest* rq = [FBRequest requestWithGraphPath:userID  parameters:params HTTPMethod:nil];
//                    [rq startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
//                        NSDictionary* dic = (NSDictionary *)result;
//                        
//                        dispatch_async(dispatch_get_main_queue(), ^{
//                            photo.caption =[dic objectForKey:@"name"];
//                        });
//                    }];
                    
                    [self.photos addObject:photo];
                }
                else
                {
                   
                    MWPhoto *photo = [MWPhoto photoWithImage:objPhoto.LargeImage];
//                    NSString * userID = [NSString stringWithFormat:@"%@",objPhoto.UserId];
//                    NSDictionary* params = [NSDictionary dictionaryWithObject:@"id,name" forKey:@"fields"];
//                    FBRequest* rq = [FBRequest requestWithGraphPath:userID  parameters:params HTTPMethod:nil];
//                    [rq startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
//                        NSDictionary* dic = (NSDictionary *)result;
//                        
//                        dispatch_async(dispatch_get_main_queue(), ^{
//                            photo.caption =[dic objectForKey:@"name"];
//                        });
//                    }];
                    
                    [self.photos addObject:photo];
                }
            }
            i++;
        }
        
        // Create & present browser
        MWPhotoBrowser *browser = [[MWPhotoBrowser alloc] initWithDelegate:self];
        // Set options
        browser.wantsFullScreenLayout = YES; // Decide if you want the photo browser full screen, i.e. whether the status bar is affected (defaults to YES)
        browser.displayActionButton = YES; // Show action button to save, copy or email photos (defaults to NO)
        [browser setInitialPageIndex:selectedIndexRow-1]; // Example: allows second image to be presented first
        browser.navigationController.title = @"Test";
        browser.title = @"Test";
        browser.navigationItem.backBarButtonItem.title = @"Test";
   
        // Present
        [self.navigationController pushViewController:browser animated:YES];
    }
    else
    {
        id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
        [tracker sendEventWithCategory:@"uiAction"
                                withAction:@"buttonPress"
                                 withLabel:@"Camera From gallery Square"
                                 withValue:[NSNumber numberWithInt:100]];
        [self getCameraPicture:self];
    }
    // TODO: Select Item
}
- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath {
    // TODO: Deselect item
}


-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    //PAPhotoDetailSegue
    if([segue.identifier isEqualToString: @"PAPhotoDetailSegue"])
    {
        //PACollectionViewCell *cell = (PACollectionViewCell *)sender;
        //NSIndexPath *indexPath = [self.collectionView indexPathForCell:cell];
        //Select the image from source
        PAImageDetailController* detailedView = (PAImageDetailController* )[segue destinationViewController];
        //detailedView.img = load image from internet;
        detailedView.PhotoEntity = (PAPhotoEntity *)[self.searchResults objectAtIndex:[selectedIndex row]];
        //                NSString *indexx = [NSString stringWithFormat:@"%i",selectedIndex.row];
        //                UIAlertView *alert = [[UIAlertView alloc]
        //                                      initWithTitle: @"indexx"
        //                                      message: indexx
        //                                      delegate: nil
        //                                      cancelButtonTitle:@"OK"
        //                                      otherButtonTitles:nil];
        //                [alert show];
        //detailedView.img = ((PAPhotoEntity *)[self.searchResults objectAtIndex:[indexPath row]]).LargeImage;
    }
    
}

- (void)viewDidUnload {
    
    [self setCollectionView:nil];
    [self setToolbarSpinner:nil];
    [self setTableView:nil];
    [super viewDidUnload];
}

- (IBAction)refresh:(id)sender {
    
    sheet = [[UIActionSheet alloc] initWithTitle:@"Options:"
                                        delegate:self
                               cancelButtonTitle:@"Cancel"
                          destructiveButtonTitle:nil
                               otherButtonTitles:@"Camera", @"Upload from gallery", @"Check for new pictures", nil];
    
    // Show the sheet
    [sheet showInView:[self.view window]];
    
    
}
- (IBAction)takePic:(id)sender
{
     id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendEventWithCategory:@"uiAction"
                        withAction:@"menuPress"
                         withLabel:@"Camera"
                         withValue:[NSNumber numberWithInt:100]];
    
    [self getCameraPicture:self];
}
-(void) actionSheet:(UIActionSheet*) actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    if(buttonIndex == 0)//Camera
    {
        [tracker sendEventWithCategory:@"uiAction"
                            withAction:@"menuPress"
                             withLabel:@"Camera"
                             withValue:[NSNumber numberWithInt:100]];

      [self getCameraPicture:self];
    }
    else if(buttonIndex == 1)//Exsiting Photo
    {
        [tracker sendEventWithCategory:@"uiAction"
                            withAction:@"menuPress"
                             withLabel:@"Load From Gallery"
                             withValue:[NSNumber numberWithInt:100]];
        [self selectExistingPicture];
    }
    else  if(buttonIndex == 2)//Refresh
    {
        [tracker sendEventWithCategory:@"uiAction"
                            withAction:@"menuPress"
                             withLabel:@"Refresh Gallery"
                             withValue:[NSNumber numberWithInt:100]];

        _currentSelectedEventID = @"";
        [self loadSelf];
    }
    else
    {
        
    }
    [sheet dismissWithClickedButtonIndex:buttonIndex animated:YES];
}

-(IBAction)getCameraPicture:(id)sender
{
       
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendEventWithCategory:@"uiAction"
                        withAction:@"buttonPress"
                         withLabel:@"CameraOpened"
                         withValue:[NSNumber numberWithInt:100]];

    UIImagePickerController *picker = [[UIImagePickerController alloc]init];
    picker.delegate = self;
    picker.allowsEditing = NO;
    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    picker.cameraFlashMode = flashMode;
    userDidUploadFromGallery = NO;
    //picker.cameraOverlayView = my view
    //picker.navigationItem.rightBarButtonItem.title = @"Upload";
    //picker.navigationBar.topItem.title = @"Upload";
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    NSString *urlString = [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Upload/UploadFile?lat=%@&lon=%@&alt=%@&userid=%@&eventid=%@&plcae=%@&eventLable=%@",appDelegate.Latitude,appDelegate.Longitude,@"0",appDelegate.user.id,@"1",@"testPlace",@"testEventlabel"];
    
    //    UIAlertView *alert = [[UIAlertView alloc]
    //                          initWithTitle: @"url is..."
    //                          message: urlString
    //                          delegate: nil
    //                          cancelButtonTitle:@"OK"
    //                          otherButtonTitles:nil];
    //    [alert show];
    
    [self presentModalViewController:picker animated:YES];
}

-(IBAction)selectExistingPicture{
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary])
    {
        UIImagePickerController *picker = [[UIImagePickerController alloc]init];
        picker.delegate = self;
        picker.allowsEditing = NO;
        picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        userDidUploadFromGallery = YES;
        [self presentModalViewController:picker animated:YES];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error accessing photo library"
                                                        message:@"Devices does not support a photo library"
                                                       delegate:nil
                                              cancelButtonTitle:@"Dismiss"
                                              otherButtonTitles:nil];
        [alert show];
    }
}
#pragma mark -
-(void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissModalViewControllerAnimated:YES];
    if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
        flashMode = picker.cameraFlashMode;
    }
    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(queue, ^{
        if (userDidUploadFromGallery == NO) {
            UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil);
        }
        
    });
    
    
    //self.imageView.image = image;
    //self.imageView.alpha = 1;
    //self.imageView.alpha = 1;
    
    
    NSData *imageData = UIImageJPEGRepresentation(image,0.5);
    
    _imageSize = imageData.length;
    
    //PAPhotoEntity
    PAPhotoEntity *newPhoto = [[PAPhotoEntity alloc] init];
    UIImage *compressedImage = [UIImage imageWithData:imageData];
    newPhoto.Thumbnail = compressedImage;
    newPhoto.LargeImage = compressedImage;
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    newPhoto.LocationName = appDelegate.EventName;
    newPhoto.UserId = appDelegate.user.id;
    [self.searchResults addObject:newPhoto];
    [self.collectionView performBatchUpdates:^
     {
         //[self.collectionView insertItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:[self.searchResults count]-1 inSection:0]]];
         NSString* index = [NSString stringWithFormat:@"%i",[self.searchResults count]-1];
         [UploadStatus setObject:@"Uploading" forKey:index];
         [self.collectionView insertItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:[self.searchResults count]-1 inSection:0]]];
     } completion:nil];
    
    if ((self.collectionView.contentSize.height - self.collectionView.bounds.size.height) > 0.0)
    {
        CGPoint bottomOffSet = CGPointMake(0, self.collectionView.contentSize.height - self.collectionView.bounds.size.height);
        
        //            NSString *contentSizeheight = [NSString stringWithFormat:@"%f",self.collectionView.contentSize.height];
        //            UIAlertView *alert = [[UIAlertView alloc]
        //                                  initWithTitle: @"self.collectionView.contentSize.height"
        //                                  message: contentSizeheight
        //                                  delegate: nil
        //                                  cancelButtonTitle:@"OK"
        //                                  otherButtonTitles:nil];
        //            [alert show];
        
        //    NSString *boundssizeheight = [NSString stringWithFormat:@"%f",self.collectionView.bounds.size.height];
        //    UIAlertView *alert2 = [[UIAlertView alloc]
        //                          initWithTitle: @"self.collectionView.bounds.size.height"
        //                          message: boundssizeheight
        //                          delegate: nil
        //                          cancelButtonTitle:@"OK"
        //                          otherButtonTitles:nil];
        //    [alert2 show];
        
        [UIView animateWithDuration:1.0 animations:^{
            [self.collectionView setContentOffset:bottomOffSet animated:YES];
        }];
    }
    [self uploadImage:imageData];
    
    //[self getCameraPicture:self];
}

-(void) imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    canceled = YES;
    [picker dismissModalViewControllerAnimated:YES];
}

-(NSString *)urlEncodeUsingEncoding:(NSStringEncoding)encoding {
	return (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(NULL,
                                                               (CFStringRef)self,
                                                               NULL,
                                                               (CFStringRef)@"!*'\"();:@&=+$,/?%#[]% ",
                                                               CFStringConvertNSStringEncodingToEncoding(encoding)));
}
- (void)uploadImage:(NSData *) imageData {
    
    
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendEventWithCategory:@"autoAction"
                        withAction:@"auto"
                         withLabel:@"Upload Photo"
                         withValue:[NSNumber numberWithInt:100]];
	/*
	 turning the image into a NSData object
	 getting the image back out of the UIImageView
	 setting the quality to 90
     */
	
	// setting up the URL to post to
	//NSString *urlString = @"http://iphone.zcentric.com/test-upload.php";
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    NSString *selectedLocationName = [NSString stringWithFormat:@"%@",appDelegate.SelectedLocationName];
  
	NSString* escapedLocationNameString =  [selectedLocationName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSString *urlString = [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Upload/UploadFile?lat=%@&lon=%@&alt=%@&heading=%@&userid=%@&eventid=%@&place=%@&eventLable=%@",appDelegate.Latitude,appDelegate.Longitude,appDelegate.Altitude,appDelegate.Heading,appDelegate.user.id,appDelegate.EventID,escapedLocationNameString,@"testEventlabel"];
    
    
	// setting up the request object now
	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
	[request setURL:[NSURL URLWithString:urlString]];
	[request setHTTPMethod:@"POST"];
	
	/*
	 add some header info now
	 we always need a boundary when we post a file
	 also we need to set the content type
	 
	 You might want to generate a random boundary.. this is just the same
	 as my output from wireshark on a valid html post
     */
	NSString *boundary = [NSString stringWithString:@"---------------------------14737809831466499882746641449"];
	NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
	[request addValue:contentType forHTTPHeaderField: @"Content-Type"];
	
	/*
	 now lets create the body of the post
     */
	NSMutableData *body = [NSMutableData data];
	[body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[[NSString stringWithString:@"Content-Disposition: form-data; name=\"userfile\"; filetype=\"image/jpeg\"; filename=\"ipodfile.jpg\"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[[NSString stringWithString:@"Content-Type: application/octet-stream\r\n\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[NSData dataWithData:imageData]];
	[body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	// setting the body of the post to the reqeust
	[request setHTTPBody:body];
    [request setTimeoutInterval:30000];
	
    [NSURLConnection  connectionWithRequest: request delegate: self];
    
}

#pragma mark -
- (void)connection:(NSURLConnection *)connection  didSendBodyData:(NSInteger)bytesWritten
 totalBytesWritten:(NSInteger)totalBytesWritten
totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite
{
    dispatch_async(dispatch_get_main_queue(), ^{
        
        NSString * index = [NSString stringWithFormat:@"%i",[self.searchResults count]-1];
        PACollectionViewCell *cell = [picturesDic objectForKey:index];
        float precentage =  ((totalBytesWritten/(float)_imageSize)*100)/100;
        
        if (precentage < 1.0)
        {
            cell.uploadImageProgress.hidden = NO;
            cell.uploadImageProgress.progress = precentage;
        }
        else
        {
            NSString* index = [NSString stringWithFormat:@"%i",[self.searchResults count]-1];
            [UploadStatus setObject:@"Done" forKey:index];
            cell.ok.hidden = NO;
            cell.uploadImageProgress.hidden = YES;
        }
    });
}

-(void) navigationController:(UINavigationController*) navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    //    UIBarButtonItem *uploadButton = [[UIBarButtonItem alloc] initWithTitle:@"Upload" style:UIBarButtonItemStylePlain target:self action:@selector(didFinishPickingMediaWithInfo)];
    //    viewController.navigationController.navigationBar.topItem.title = @"Upload?";
    //    [viewController.navigationController.navigationBar.topItem setRightBarButtonItem:uploadButton];
}

#pragma mark -
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*) response;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        
        //        dispatch_async(dispatch_get_main_queue(), ^{
        //                        UIAlertView *alert = [[UIAlertView alloc]
        //                                              initWithTitle: @"didReceiveResponse"
        //                                              message: [NSString stringWithFormat:@"%i",[httpResponse statusCode]]
        //                                              delegate: nil
        //                                              cancelButtonTitle:@"OK"
        //                                              otherButtonTitles:nil];
        //                        [alert show];
        //                    });
        
    });
}
#pragma mark -
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
}
#pragma mark -
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    //    UIAlertView *alert = [[UIAlertView alloc]
    //                          initWithTitle: @"Upload"
    //                  message: @"OK"
    //                delegate: nil
    //       cancelButtonTitle:@"OK"
    //      otherButtonTitles:nil];
    //    [alert show];
}
#pragma mark -
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error;
{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle: @"Upload"
                          message: @"FAIL"
                          delegate: nil
                          cancelButtonTitle:@"OK"
                          otherButtonTitles:nil];
    [alert show];
}


-(void) actuallyTwittePhoto:(id<MWPhoto>)photo
{
    if([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        
        
        SLComposeViewController *slComposeViewController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        NSString* eventID = appDelegate.EventID;

        [slComposeViewController addImage:[photo underlyingImage]];
        NSString* url = [NSString stringWithFormat: @"http://www.picaround.com/Gallery/GalleryByEventId?eventId=%@",eventID];
        [slComposeViewController addURL:[NSURL URLWithString:url]];
        [slComposeViewController setInitialText:@"Check out PicAround"];
        [self presentViewController:slComposeViewController animated:YES completion:NULL];
    }
}


- (void)actuallyEmailPhoto:(id<MWPhoto>)photo {
    if ([photo underlyingImage]) {
        MFMailComposeViewController *emailer = [[MFMailComposeViewController alloc] init];
        emailer.mailComposeDelegate = self;
        [emailer setSubject:@"PicAround has something to share with you."];
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        NSString* eventID = appDelegate.EventID;
        NSString* emailBody = [NSString stringWithFormat: @"Picaround has a picture to share with you \r\n http://www.picaround.com/Gallery/GalleryByEventId?eventId=%@",eventID];
        [emailer setMessageBody:emailBody isHTML:NO];
        
        [emailer addAttachmentData:UIImagePNGRepresentation([photo underlyingImage]) mimeType:@"png" fileName:@"PicAroundPhoto.png"];
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            emailer.modalPresentationStyle = UIModalPresentationPageSheet;
        }
        [self presentModalViewController:emailer animated:YES];
         //      [self hideProgressHUD:NO];
        
    }
}


-(void) actuallyPostToFacebook:(id<MWPhoto>)photo
{
    if([SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook])
    {
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        NSString* eventID = appDelegate.EventID;

        
        SLComposeViewController *slComposeViewController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeFacebook];
        [slComposeViewController addImage:[photo underlyingImage]];
        NSString* url = [NSString stringWithFormat: @"http://www.picaround.com/Gallery/GalleryByEventId?eventId=%@",eventID];
        [slComposeViewController addURL:[NSURL URLWithString:url]];
        [slComposeViewController setInitialText:@"Check out PicAround"];
        [self presentViewController:slComposeViewController animated:YES completion:NULL];
    }
}

-(void) actuallyShareBySMS:(id<MWPhoto>)photo
{
    MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc] init];
	if([MFMessageComposeViewController canSendText])
	{
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        NSString* eventID = appDelegate.EventID;

        
        NSString* smsBody = [NSString stringWithFormat: @"Picaround has a picture to share with you \r\n http://www.picaround.com/Gallery/GalleryByEventId?eventId=%@",eventID];
		controller.body = smsBody;
		//controller.recipients = [NSArray arrayWithObjects:@"12345678", @"87654321", nil];
		controller.messageComposeDelegate = self;
		[self presentModalViewController:controller animated:YES];
	}
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
	switch (result)
    {
		case MessageComposeResultCancelled:
			NSLog(@"Cancelled");
			break;
		case MessageComposeResultFailed:
        {
			UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Failure" message:@"Your device doesn't support sharing via SMS"
                                                            delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
        }
			break;
		case MessageComposeResultSent:
			break;
		default:
			break;
	}
    
	[self dismissModalViewControllerAnimated:YES];
}

- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    switch (result)
    {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled: you cancelled the operation and no email message was queued.");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved: you saved the email message in the drafts folder.");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail send: the email message is queued in the outbox. It is ready to send.");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail failed: the email message was not saved or queued, possibly due to an error.");
            break;
        default:
            NSLog(@"Mail not sent.");
            break;
    }
    // Remove the mail view
    [self dismissModalViewControllerAnimated:YES];
}
//ADD ItemHighlited
//        NSString *totalBytesWrittenString = [NSString stringWithFormat:@"%f",precentage];
//        UIAlertView *alert = [[UIAlertView alloc]
//                              initWithTitle: @"update"
//                              message: totalBytesWrittenString
//                              delegate: nil
//                              cancelButtonTitle:@"OK"
//                              otherButtonTitles:nil];
//        [alert show];

- (MWCaptionView *)photoBrowser:(MWPhotoBrowser *)photoBrowser captionViewForPhotoAtIndex:(NSUInteger)index {
    
    MWPhoto *photo = [self.photos objectAtIndex:index];
    MPACaptionView *captionView = [[MPACaptionView alloc] initWithPhoto:photo];
    
    PAPhotoEntity * enity = [self.searchResults objectAtIndex:(index+1)];
    NSString * userID = [NSString stringWithFormat:@"%@",enity.UserId];
    NSDictionary* params = [NSDictionary dictionaryWithObject:@"id,name" forKey:@"fields"];
    FBRequest* rq = [FBRequest requestWithGraphPath:userID  parameters:params HTTPMethod:nil];
    [rq startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
        NSDictionary* dic = (NSDictionary *)result;
        
        dispatch_async(dispatch_get_main_queue(), ^{
//
//            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0,-200,
//                                                                       100l,
//                                                                       100)];
//            label.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
//            label.opaque = NO;
//            label.backgroundColor = [UIColor clearColor];
//            label.textAlignment = UITextAlignmentCenter;
//            label.lineBreakMode = UILineBreakModeWordWrap;
//            label.numberOfLines = 3;
//            label.textColor = [UIColor whiteColor];
//            label.shadowColor = [UIColor blackColor];
//            label.shadowOffset = CGSizeMake(1, 1);
//            label.font = [UIFont systemFontOfSize:17];

            [captionView setupCaptionCustomLabel:[dic objectForKey:@"name"] subText:enity.LocationName];
//            [captionView addSubview:label];
        });
    }];
    
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(queue, ^{
 
        NSString* url = [NSString stringWithFormat:@"http://graph.facebook.com/%@/picture?type=large",userID];
        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]
                                              options:0
                                                error:nil];
        UIImage *image = [UIImage imageWithData:imageData];
        UIImageView* imageView = [[UIImageView alloc] initWithImage:image];
        [captionView setupCaptionCustomImage:imageView];
    });
    
    return captionView;
}

@end
