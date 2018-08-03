//
//  GalleryView.h
//  PicAround
//
//  Created by BenA on 2/10/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <Social/Social.h>
#import <MessageUI/MessageUI.h>
#import "PAPhotoEntity.h"
#import "PicAroundApi.h"
#import "PicAroundAppDelegate.h"
#import "MWPhotoBrowser.h"
#import "MPACaptionView.h"
#import "GAI.h"
@interface GalleryView : GAITrackedViewController<UICollectionViewDataSource,UICollectionViewDelegate,UIImagePickerControllerDelegate, NSURLConnectionDataDelegate, NSURLConnectionDelegate,UIActionSheetDelegate,MFMailComposeViewControllerDelegate , MFMessageComposeViewControllerDelegate,MWPhotoBrowserDelegate>

{
    NSMutableDictionary *picturesDic;
    NSMutableDictionary *UploadStatus;
    //NSMutableArray *photos;
    __weak IBOutlet UILabel *cameraViewHeader;
    BOOL canceled;
    UIImagePickerControllerCameraFlashMode flashMode;
    
    IBOutlet UIButton *Camera;
    NSIndexPath *selectedIndex;
    NSInteger selectedIndexRow;
    UIActionSheet *sheet;
    BOOL userDidUploadFromGallery;
}
- (IBAction)takePic:(id)sender;

- (IBAction)refresh:(id)sender;

@property (strong, nonatomic) UIRefreshControl *refreshControl;
@property (weak, nonatomic) IBOutlet UICollectionView *tableView;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *toolbarSpinner;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UINavigationItem *galleryNavigation;
@property (nonatomic, strong) PicAroundApi *paApi;
@property (nonatomic, strong) NSMutableArray *searchResults;
@property (nonatomic, strong) NSMutableArray *searches;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property NSString* currentSelectedEventID;
@property (nonatomic, retain) NSMutableArray *photos;

- (void)connection:(NSURLConnection *)connection   didSendBodyData:(NSInteger)bytesWritten
 totalBytesWritten:(NSInteger)totalBytesWritten
totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite;

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response;

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data;

- (void)connectionDidFinishLoading:(NSURLConnection *)connection;

-(IBAction)getCameraPicture:(id)sender;
-(IBAction)selectExistingPicture;

@property (nonatomic) NSUInteger imageSize;

- (NSUInteger)numberOfPhotosInPhotoBrowser:(MWPhotoBrowser *)photoBrowser;
- (MWPhoto *)photoBrowser:(MWPhotoBrowser *)photoBrowser photoAtIndex:(NSUInteger)index;
-(void) actuallyShareBySMS:(id<MWPhoto>)photo;
-(void) actuallyPostToFacebook:(id<MWPhoto>)photo;
- (void)actuallyEmailPhoto:(id<MWPhoto>)photo;
-(void) actuallyTwittePhoto:(id<MWPhoto>)photo;

@end
